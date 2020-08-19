package com.hani.testing.models

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "notes")

class Note : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id = 0

    @NonNull
    @ColumnInfo(name = "title")
    var title: String? = null

    @ColumnInfo(name = "content")
    var content: String? = null

    @ColumnInfo(name = "timestamp")
    var timestamp: String? = null

    constructor(title: String, content: String?, timestamp: String?) {
        this.title = title
        this.content = content
        this.timestamp = timestamp
    }

    @Ignore
    constructor(id: Int, title: String, content: String?, timestamp: String?) {
        this.id = id
        this.title = title
        this.content = content
        this.timestamp = timestamp
    }

    @Ignore
    constructor() {
    }

    @Ignore
    constructor(note: Note) {
        id = note.id
        title = note.title
        content = note.content
        timestamp = note.timestamp
    }

    protected constructor(`in`: Parcel) {
        id = `in`.readInt()
        title = `in`.readString()!!
        content = `in`.readString()
        timestamp = `in`.readString()
    }



    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(title)
        dest.writeString(content)
        dest.writeString(timestamp)
    }

    override fun equals(obj: Any?): Boolean {
        if (obj == null) {
            return false
        }
        if (javaClass != obj.javaClass) {
            return false
        }
         val note = obj as Note
        return note.id == id && note.title == title && note.content == content

    }

    override fun toString(): String {
        return "Note(id=$id, title=$title, content=$content, timestamp=$timestamp)"
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Note> =
            object : Parcelable.Creator<Note> {
                override fun createFromParcel(`in`: Parcel): Note? {
                    return Note(`in`)
                }

                override fun newArray(size: Int): Array<Note?> {
                    return arrayOfNulls(size)
                }
            }
    }
}