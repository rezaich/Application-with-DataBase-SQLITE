package com.plete.addrecord

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.hardware.input.InputManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_update.*
import kotlinx.android.synthetic.main.view_data.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupListOfDataIntoRecyclerView()
        btnAddRecord.setOnClickListener {
            addRecord()
            closeKeyboard()
            setupListOfDataIntoRecyclerView()
        }
    }

    private fun addRecord(){
        val nama = namatxt.text.toString()
        val email = emailtxt.text.toString()
        val phone = phonetxt.text.toString()
        val address = addresstxt.text.toString()
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)

        if (!nama.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !address.isEmpty()){
            val status = databaseHandler.addEmployee(EMPModel(0, nama, email, phone, address))
            if (status > -1){
                Toast.makeText(this, "Record Saved", Toast.LENGTH_LONG).show()
                namatxt.text.clear()
                emailtxt.text.clear()
                phonetxt.text.clear()
                addresstxt.text.clear()
            }
        } else {
            Toast.makeText(this, "Nama or Email gak bisa Null", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * method untuk mendapatkan jumlah record
     */

    private fun getItemsList(): ArrayList<EMPModel>{
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val empList: ArrayList<EMPModel> = databaseHandler.viewEmployee()
        return empList
    }

    /**
     * method untuk menampilkan emplist ke recycleview atau refresh data
     */

    private fun setupListOfDataIntoRecyclerView(){
        if (getItemsList().size > 0){
            rv_data.visibility = View.VISIBLE
            noRecord.visibility = View.GONE

            rv_data.layoutManager = LinearLayoutManager(this)
            val itemAdapter = ItemAdapter(this, getItemsList())
            rv_data.adapter = itemAdapter
        } else {
            rv_data.visibility = View.GONE
            noRecord.visibility = View.VISIBLE
        }
    }

    /**
     * method untuk menampilkan dialog konfirmasi delete
     */
    fun deleteRecordAlertDialog(empModel: EMPModel){
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Delete Record")
        builder.setMessage("Are u sure?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //menampilkan tombol yes
        builder.setPositiveButton("Yes") {dialog: DialogInterface, which: Int ->
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            val status = databaseHandler.deleteEmployee(EMPModel(empModel.id,"", "", empModel.phone, ""))
            if (status > -1){
                Toast.makeText(this, "Record deleted successfully", Toast.LENGTH_LONG).show()
            }
            dialog.dismiss()
            setupListOfDataIntoRecyclerView()
        }
        //menampilkan tombol yes
        builder.setNegativeButton("No") {dialog: DialogInterface, which: Int ->
            dialog.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        //memastikan user menekan yes no
        alertDialog.setCancelable(false)
        //nampilin kotak dialog
        alertDialog.show()
    }
    /**
     * method cls keyborad
     */
    fun closeKeyboard(){
        val view = this.currentFocus
        if (view != null){
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    /**
     * method to show custom update dialog
     */
    fun updateRecordDialog(empModel: EMPModel){
        val updateDialog = Dialog(this, R.style.Theme_Dialog)

        updateDialog.setCancelable(false)
        updateDialog.setContentView(R.layout.dialog_update)

        updateDialog.etUpdateName.setText(empModel.nama)
        updateDialog.etUpdateEmail.setText(empModel.email)
        updateDialog.etUpdatePhone.setText(empModel.phone)
        updateDialog.etUpdateAddress.setText(empModel.address)


        updateDialog.tvUpdate.setOnClickListener {
            val nama = updateDialog.etUpdateName.text.toString()
            val email = updateDialog.etUpdateEmail.text.toString()
            val phone = updateDialog.etUpdatePhone.text.toString()
            val address = updateDialog.etUpdateAddress.text.toString()

            val databaseHandler: DatabaseHandler = DatabaseHandler(this)

            if (!nama.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !address.isEmpty()){
                val status = databaseHandler.updateEmployee(EMPModel(empModel.id, nama, email, empModel.phone, address))
                if (status > -1){
                    Toast.makeText(this, "Record Updated", Toast.LENGTH_LONG).show()
                    setupListOfDataIntoRecyclerView()
                    closeKeyboard()
                    updateDialog.dismiss()
                }
            } else{
                Toast.makeText(this, "Cannot blank", Toast.LENGTH_LONG).show()
            }
        }
        updateDialog.tvCancel.setOnClickListener {
            updateDialog.dismiss()
        }
        updateDialog.show()
    }

    fun Expands(empModel: EMPModel){
        val updateDialog = Dialog(this, R.style.Theme_Dialog)

        updateDialog.setCancelable(false)
        updateDialog.setContentView(R.layout.view_data)

        updateDialog.vwDataNama.setText(empModel.nama)
        updateDialog.vwDataEmail.setText(empModel.email)
        updateDialog.vwDataPhone.setText(empModel.phone)
        updateDialog.vwDataAddress.setText(empModel.address)

        updateDialog.vwOkay.setOnClickListener {
            updateDialog.dismiss()
        }
        updateDialog.show()
    }
}