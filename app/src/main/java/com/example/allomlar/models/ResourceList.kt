package com.mac.allomalar.models

data class ResourceList<out T>(
    val status: Status,
    val data: List<T?>?,
    val message: String?
) {
    companion object {
        fun <T> success(data: List<T?>?): ResourceList<T> {
            return ResourceList(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: List<T?>?): ResourceList<T> {
            return ResourceList(Status.ERROR, data, msg)
        }

        fun <T> loading(data: List<T?>?): ResourceList<T> {
            return ResourceList(Status.LOADING, data, null)
        }

    }
}