package com.fastcampus.calculator

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fastcampus.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var isOperator = false

    private var hasOperator = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        // TODO: 2021-07-05 tvResult에 실시간으로 결과값 계산
    }

    private fun operatorButtonClicked(op: String) {

    }

    fun resultButtonClicked(v: View) {

    }

    fun clearButtonClicked(v: View) {

    }

    fun historyButtonClicked(v: View) {

    }
}