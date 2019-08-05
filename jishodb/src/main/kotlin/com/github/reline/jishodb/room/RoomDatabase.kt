package com.github.reline.jishodb.room

class RoomDatabase(val entities: List<RoomEntity>) {
    fun createSql(): String {
        val builder = StringBuilder()
        entities.forEach { entity ->
            builder.append(entity.createSql.replace(PLACEHOLDER, entity.tableName)).append(";\n")
            entity.indices.forEach { index ->
                builder.append(index.createSql
                        .replace(CREATE_INDEX, CREATE_INDEX_IF_NOT_EXISTS)
                        .replace(PLACEHOLDER, entity.tableName)
                ).append(";\n")
            }
        }
        return builder.toString()
    }

    companion object {
        private const val CREATE_INDEX = "CREATE  INDEX"
        private const val CREATE_INDEX_IF_NOT_EXISTS = "CREATE INDEX IF NOT EXISTS"
        private const val PLACEHOLDER = "`\${TABLE_NAME}`"
    }
}
