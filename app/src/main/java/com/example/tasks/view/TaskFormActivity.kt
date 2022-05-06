package com.example.tasks.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.R
import com.example.tasks.databinding.ActivityTaskFormBinding
import com.example.tasks.service.constants.TaskConstants
import com.example.tasks.service.model.TaskModel
import com.example.tasks.viewmodel.TaskFormViewModel
import kotlinx.android.synthetic.main.activity_task_form.*
import java.text.SimpleDateFormat
import java.util.*

class TaskFormActivity : AppCompatActivity(), View.OnClickListener,
    DatePickerDialog.OnDateSetListener {

    private lateinit var binding: ActivityTaskFormBinding
    private lateinit var mViewModel: TaskFormViewModel
    private val listPriorityId: MutableList<Int> = arrayListOf()
    private var mTaksId = 0

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTaskFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mViewModel = ViewModelProvider(this).get(TaskFormViewModel::class.java)

        // Inicializa eventos
        listeners()
        observe()

        mViewModel.listPriorities()

        loadDataFromActivity()
    }

    override fun onClick(view: View) {
        val id = view.id
        if (id == R.id.button_save) {
            handleSave()
        } else if (id == R.id.button_date) {
            showDatePicker()
        }
    }

    private fun loadDataFromActivity() {
        //mexer no id que veio do bundle de alltasks
        val bundle = intent.extras
        if (bundle != null) {
            mTaksId = bundle.getInt(TaskConstants.BUNDLE.TASKID) //id da tarefa
            mViewModel.load(mTaksId)
            button_save.text = getString(R.string.update_task)
        }
    }

    private fun handleSave() {
        val task = TaskModel().apply {
            this.id = mTaksId
            this.description = binding.editDescription.text.toString()
            this.complete = binding.checkComplete.isChecked
            this.dueDate = binding.buttonDate.text.toString()
            this.priorityId = listPriorityId[spinner_priority.selectedItemPosition]
        }
        mViewModel.save(task)
    }


    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, this, year, month, day).show()
    }

    private fun observe() {
        mViewModel.priorityList.observe(this, androidx.lifecycle.Observer {
            val list: MutableList<String> = arrayListOf()
            for (item in it) {
                list.add(item.description)
                listPriorityId.add(item.id)
            }
            val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, list)

            binding.spinnerPriority.adapter = adapter

        })

        mViewModel.task.observe(this, androidx.lifecycle.Observer {
            edit_description.setText(it.description)
            check_complete.isChecked = it.complete
            spinner_priority.setSelection(getIndex(it.priorityId)) //getIndex pois o id pode ser diferente da posição

            val date = SimpleDateFormat("yyyy-MM-dd").parse(it.dueDate)
            button_date.text = dateFormat.format(date)
        })

        mViewModel.validation.observe(this, androidx.lifecycle.Observer {
            if (it.success()) {
                if (mTaksId == 0) {
                    toast(getString(R.string.task_created))
                } else {
                    toast(getString(R.string.task_updated))
                }
                finish()
            } else {
                toast(it.faluire())
            }
        })
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun getIndex(priorityId: Int): Int {
        var index = 0
        for (i in 0 until listPriorityId.count()) {
            if (listPriorityId[i] == priorityId) {
                index = i
                break
            }
        }
        return index
    }

    private fun listeners() {
        binding.buttonSave.setOnClickListener(this)
        //button_save.setOnClickListener(this)
        binding.buttonDate.setOnClickListener(this)
    }

    //escolhe minha data
    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)

        val str = dateFormat.format(calendar.time)
        binding.buttonDate.text = str
    }

}
