package com.appl.fastnote_ref.FileReader

import android.content.Context
import java.io.File

class NormalFileReader: FileReader() {
    override fun getMemoFileSuffix(): String {
        return "n.txt"
    }
}