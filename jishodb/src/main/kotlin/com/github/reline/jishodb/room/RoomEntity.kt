package com.github.reline.jishodb.room

class RoomEntity(
        val tableName: String,
        val createSql: String,
        val indices: List<RoomIndex>
)
