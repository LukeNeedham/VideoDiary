package com.lukeneedham.videodiary.data.persistence.model

enum class OrientationPersistence(val persistenceId: String) {
    Portrait("Portrait"),
    Landscape("Landscape");

    companion object {
        fun fromId(id: String) = entries.first { it.persistenceId == id }
    }
}