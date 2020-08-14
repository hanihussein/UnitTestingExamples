package com.hani.testing.ui

class Resource<T>(
    val status: Status,
    val data: T?,
    message: String?
) {
    val message: String?

    enum class Status {
        SUCCESS, ERROR, LOADING
    }

    override fun equals(obj: Any?): Boolean {

        if (obj != null) {

            if (obj.javaClass != javaClass || obj.javaClass != Resource::class.java) {
                return false
            }
            val resource: Resource<*>? = obj as Resource<*>?
            if (resource!!.status != status) {
                return false
            }
            if (data != null) {
                if (resource.data !== data) {
                    return false
                }
            }
            if (resource.message != null) {
                if (message == null) {
                    return false
                }
                if (resource.message != message) {
                    return false
                }
            }
            return true
        }
        return false
    }

    companion object {
        fun <T> success(data: T, message: String): Resource<T> {
            return Resource(
                Status.SUCCESS,
                data,
                message
            )
        }

        fun <T> error(data: T?, msg: String): Resource<T?> {
            return Resource(
                Status.ERROR,
                data,
                msg
            )
        }

        fun <T> loading(data: T?): Resource<T?> {
            return Resource(
                Status.LOADING,
                data,
                null
            )
        }
    }

    init {
        this.message = message
    }
}