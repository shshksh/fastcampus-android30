package com.fastcampus.tinder

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.fastcampus.tinder.DBKey.Companion.DIS_LIKE
import com.fastcampus.tinder.DBKey.Companion.LIKE
import com.fastcampus.tinder.DBKey.Companion.LIKED_BY
import com.fastcampus.tinder.DBKey.Companion.MATCH
import com.fastcampus.tinder.DBKey.Companion.NAME
import com.fastcampus.tinder.DBKey.Companion.USERS
import com.fastcampus.tinder.DBKey.Companion.USER_ID
import com.fastcampus.tinder.databinding.ActivityLikeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction

class LikeActivity : AppCompatActivity(), CardStackListener {

    private lateinit var binding: ActivityLikeBinding

    private val auth by lazy { FirebaseAuth.getInstance() }

    private val userDB by lazy { Firebase.database.reference.child(USERS) }

    private val cardItemAdapter by lazy { CardItemAdapter() }
    private val cardLayoutManager by lazy { CardStackLayoutManager(this, this) }
    private val cardItems = mutableListOf<CardItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLikeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val currentUserDB = userDB.child(getCurrentUserID())
        currentUserDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(NAME).value == null) {
                    showNameInputDialog()
                    return
                }

                getUnselectedUsers()
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        initViews()
    }

    private fun initViews() {
        binding.cardStackView.apply {
            layoutManager = cardLayoutManager
            adapter = cardItemAdapter
        }

        binding.btnSignOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.btnMatchList.setOnClickListener {
            startActivity(Intent(this, MatchedUserActivity::class.java))
        }
    }

    private fun getUnselectedUsers() {
        userDB.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if (isUnselectedUser(snapshot)) {
                    addNewUser(snapshot)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                updateUserName(snapshot)
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun isUnselectedUser(snapshot: DataSnapshot) =
        (snapshot.child(USER_ID).value != getCurrentUserID()
                && snapshot.child(LIKED_BY).child(LIKE).hasChild(getCurrentUserID()).not()
                && snapshot.child(LIKED_BY).child(DIS_LIKE).hasChild(getCurrentUserID()).not())

    private fun addNewUser(snapshot: DataSnapshot) {
        val userId = snapshot.child(USER_ID).value.toString()
        var name = "undecided"
        if (snapshot.child(NAME).value != null) {
            name = snapshot.child(NAME).value.toString()
        }

        cardItems.add(CardItem(userId, name))
        cardItemAdapter.submitList(cardItems)
    }

    private fun updateUserName(snapshot: DataSnapshot) {
        cardItems.find { it.userId == snapshot.key }?.let {
            it.name = snapshot.child(NAME).value.toString()
            cardItemAdapter.submitList(cardItems)
        }
    }

    private fun showNameInputDialog() {
        val editText = EditText(this)

        AlertDialog.Builder(this)
            .setTitle(getString(R.string.write_name))
            .setView(editText)
            .setPositiveButton("저장") { _, _ ->
                if (editText.text.isEmpty())
                    showNameInputDialog()
                else
                    saveUserName(editText.text.toString())
            }
            .setCancelable(false)
            .show()
    }

    private fun saveUserName(name: String) {
        val userId = getCurrentUserID()
        val currentUserDB = userDB.child(userId)
        val user = mutableMapOf<String, Any>()
        user[USER_ID] = userId
        user[NAME] = name
        currentUserDB.updateChildren(user)

        getUnselectedUsers()
    }

    private fun getCurrentUserID(): String {
        if (auth.currentUser == null) {
            showToast("로그인이 되어있지 않습니다.")
            finish()
        }

        return auth.currentUser?.uid.orEmpty()
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {}

    override fun onCardSwiped(direction: Direction?) {
        when (direction) {
            Direction.Right -> like()
            Direction.Left -> disLike()
            else -> {
            }
        }
    }

    private fun like() {
        val card = cardItems[cardLayoutManager.topPosition - 1]
        cardItems.removeFirst()

        userDB.child(card.userId)
            .child(LIKED_BY)
            .child(LIKE)
            .child(getCurrentUserID())
            .setValue(true)

        saveMatchIfOtherUserLikedMe(card.userId)

        showToast("${card.name}님을 Like 하셨습니다.")
    }

    private fun saveMatchIfOtherUserLikedMe(otherUserId: String) {
        val otherUserDB = userDB.child(getCurrentUserID())
            .child(LIKED_BY)
            .child(LIKE)
            .child(otherUserId)

        otherUserDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value == true) {
                    updateMatchedUser(getCurrentUserID(), otherUserId)
                    updateMatchedUser(otherUserId, getCurrentUserID())
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun updateMatchedUser(from: String, to: String) {
        userDB.child(from)
            .child(LIKED_BY)
            .child(MATCH)
            .child(to)
            .setValue(true)
    }

    private fun disLike() {
        val card = cardItems[cardLayoutManager.topPosition - 1]
        cardItems.removeFirst()

        userDB.child(card.userId)
            .child(LIKED_BY)
            .child(DIS_LIKE)
            .child(getCurrentUserID())
            .setValue(true)

        showToast("${card.name}님을 disLike 하셨습니다.")
    }

    override fun onCardRewound() {}

    override fun onCardCanceled() {}

    override fun onCardAppeared(view: View?, position: Int) {}

    override fun onCardDisappeared(view: View?, position: Int) {}
}