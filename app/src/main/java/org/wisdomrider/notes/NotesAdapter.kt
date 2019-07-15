package org.wisdomrider.notes

import android.app.Dialog
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

/*
   Created By WisdomRider(Avishek Adhikari)
    Email : avishekzone@gmail.com
    Make Sure to Star Me On Github :
       https://github.com/wisdomrider/SqliteClosedHelper
     Credit Me SomeWhere In Your Project :)
     Thanks !!
*/
class NotesAdapter(internal var home: Home) : RecyclerView.Adapter<NotesAdapter.WisdomHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): WisdomHolder {
        val v = View.inflate(viewGroup.context, R.layout.note, null)
        return WisdomHolder(v)
    }


    override fun onBindViewHolder(wisdomHolder: WisdomHolder, i: Int) {
        val note = home.notes[i]
        wisdomHolder.title.text = note.title
        wisdomHolder.content.text = note.desc.replace("**", "'")
        wisdomHolder.title.tag = note._id
        val inputFormat = SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.US)
        inputFormat.setTimeZone(TimeZone.getTimeZone("Etc/UTC"))
        val outputFormat = SimpleDateFormat("dd MMMM yyyy")
        val date = inputFormat.parse(note.createdAt.replace("T", " ").replace("Z", "").split(".")[0])
        val outputText = "- " + outputFormat.format(date)
        wisdomHolder.date.text = outputText

        val listerner = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus || !home.showDialog) {
                home.showDialog = true
                return@OnFocusChangeListener
            }
            val dialog = Dialog(home)
            dialog.setContentView(R.layout.note)
            dialog.findViewById<EditText>(R.id.title).setText(note.title)
            dialog.findViewById<EditText>(R.id.title).tag = note._id
            dialog.findViewById<TextView>(R.id.date).setText(outputText)
            dialog.findViewById<EditText>(R.id.desc).setText(note.desc.replace("**", "'"))
            dialog.show()
        }
        wisdomHolder.content.onFocusChangeListener = listerner
        wisdomHolder.title.onFocusChangeListener = listerner
    }

    override fun getItemCount(): Int {
        return home.notes.size
    }

    inner class WisdomHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var title: TextView = itemView.findViewById(R.id.title)
        internal var content: TextView = itemView.findViewById(R.id.desc)
        internal var date: TextView = itemView.findViewById(R.id.date)


    }
}
