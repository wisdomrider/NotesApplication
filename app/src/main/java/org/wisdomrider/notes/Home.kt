package org.wisdomrider.notes

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*

class Home : BaseActivity() {

    var showDialog = false
    lateinit var notes: ArrayList<LoginPage.NoteData>
    lateinit var adapter: NotesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        notes = helper.getAll(LoginPage.NoteData())
        notes.reverse()
        adapter = NotesAdapter(this)
        recycle.adapter = adapter
        recycle.layoutManager = LinearLayoutManager(this)

        fab.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.note)
            val date = Date().toLocaleString().split(" ")
            val date1 = date[0] + " " + date[1] + " " + date[2]
            dialog.findViewById<TextView>(R.id.date).text = "- ${date1}"
            dialog.show()

        }
        checkForPermission()

        if (preferences.getString("add_text", "")!!.isNotEmpty()) {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.note)
            dialog.findViewById<EditText>(R.id.desc).setText(preferences.getString("add_text", ""))
            val date = Date().toLocaleString().split(" ")
            val date1 = date[0] + " " + date[1] + " " + date[2]
            dialog.findViewById<TextView>(R.id.date).text = "- ${date1}"
            dialog.show()
            preferences.putString("add_text", "").apply()

        }

    }

    private fun checkForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && !Settings.canDrawOverlays(this)
        ) {
            wisdom.toast("Enable this feature to save note anywhere !")
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            startActivityForResult(intent, 1)
        } else {
            startService(Intent(this, ChatHead::class.java))
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            checkForPermission()
        }
    }

}
