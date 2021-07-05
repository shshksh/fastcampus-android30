package com.fastcampus.calculator

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.room.Room
import com.fastcampus.calculator.databinding.ActivityMainBinding
import com.fastcampus.calculator.databinding.ItemHistoryBinding
import com.fastcampus.calculator.model.History

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var db: AppDatabase

    private var isOperator = false
    private var hasOperator = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "historyDB"
        ).build()
    }

    fun buttonClicked(v: View) {
        when (v.id) {
            R.id.btn_num0 -> numberButtonClicked("0")
            R.id.btn_num1 -> numberButtonClicked("1")
            R.id.btn_num2 -> numberButtonClicked("2")
            R.id.btn_num3 -> numberButtonClicked("3")
            R.id.btn_num4 -> numberButtonClicked("4")
            R.id.btn_num5 -> numberButtonClicked("5")
            R.id.btn_num6 -> numberButtonClicked("6")
            R.id.btn_num7 -> numberButtonClicked("7")
            R.id.btn_num8 -> numberButtonClicked("8")
            R.id.btn_num9 -> numberButtonClicked("9")
            R.id.btn_opPlus -> operatorButtonClicked("+")
            R.id.btn_opMinus -> operatorButtonClicked("-")
            R.id.btn_opMulti -> operatorButtonClicked("*")
            R.id.btn_opDiv -> operatorButtonClicked("/")
            R.id.btn_opModulo -> operatorButtonClicked("%")
        }
    }

    private fun numberButtonClicked(number: String) {

        if (isOperator) {
            binding.tvExpression.append(" ")
            isOperator = false
        }

        val expressionText = binding.tvExpression.text.split(" ")

        if (expressionText.isNotEmpty() && expressionText.last().length >= 15) {
            Toast.makeText(this, "15자리 까지만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
            return
        } else if (expressionText.last().isEmpty() && number == "0") {
            Toast.makeText(this, "0은 제일 앞에 올 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        binding.tvExpression.append(number)
        binding.tvResult.text = calculateExpression()
    }

    private fun operatorButtonClicked(op: String) {
        if (binding.tvExpression.text.isEmpty()) {
            return
        }

        when {
            isOperator -> {
                val text = binding.tvExpression.text.toString()
                binding.tvExpression.text = text.dropLast(1).plus(op)
            }
            hasOperator -> {
                Toast.makeText(this, "연산자는 한 번만 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return
            }
            else -> {
                binding.tvExpression.append(" $op")
            }
        }

        val ssb = SpannableStringBuilder(binding.tvExpression.text)
        ssb.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.green)),
            binding.tvExpression.length() - 1,
            binding.tvExpression.length(),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvExpression.text = ssb

        isOperator = true
        hasOperator = true
    }

    fun resultButtonClicked(v: View) {
        val expressionText = binding.tvExpression.text.split(" ")

        if (binding.tvExpression.text.isEmpty() || expressionText.size == 1) {
            return
        }
        if (expressionText.size != 3 && hasOperator) {
            Toast.makeText(this, "아직 완성되지 않은 수식입니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if (expressionText[0].isNumber().not() || expressionText[2].isNumber().not()) {
            Toast.makeText(this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val expression = binding.tvExpression.text.toString()
        val result = binding.tvResult.text.toString()

        binding.tvExpression.text = result
        binding.tvResult.text = ""

        isOperator = false
        hasOperator = false

        Thread {
            db.historyDao().insertHistory(History(null, expression, result))
        }.start()
    }

    private fun calculateExpression(): String {
        val expressionText = binding.tvExpression.text.split(" ")

        if (hasOperator.not() || expressionText.size != 3) {
            return ""
        } else if (expressionText[0].isNumber().not() || expressionText[2].isNumber().not()) {
            return ""
        }

        val operand1 = expressionText[0].toBigInteger()
        val operand2 = expressionText[2].toBigInteger()

        return when (expressionText[1]) {
            "+" -> operand1.add(operand2).toString()
            "-" -> operand1.subtract(operand2).toString()
            "*" -> operand1.multiply(operand2).toString()
            "/" -> operand1.divide(operand2).toString()
            "%" -> operand1.mod(operand2).toString()
            else -> ""
        }
    }

    fun clearButtonClicked(v: View) {
        binding.tvResult.text = ""
        binding.tvExpression.text = ""
        isOperator = false
        hasOperator = false
    }

    fun historyButtonClicked(v: View) {
        binding.layoutHistory.isVisible = true
        binding.layoutHistoryLinear.removeAllViews()

        Thread {
            db.historyDao().getAll().reversed().forEach { history ->
                runOnUiThread {
                    val historyBinding = ItemHistoryBinding.inflate(layoutInflater)
                    historyBinding.tvExpression.text = history.expression
                    historyBinding.tvResult.text = "= ${history.result}"

                    binding.layoutHistoryLinear.addView(historyBinding.root)
                }
            }
        }.start()
    }

    fun closeHistoryButtonClicked(v: View) {
        binding.layoutHistory.isVisible = false
    }

    fun clearHistoryButtonClicked(v: View) {
        // TODO: 2021-07-05 DB에서 모든 기록 삭제
        // TODO: 2021-07-05 뷰에서 모든 기록 삭제
    }
}

fun String.isNumber(): Boolean = this.toBigIntegerOrNull() != null