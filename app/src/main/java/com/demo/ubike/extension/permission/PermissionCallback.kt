package com.demo.ubike.extension.permission

interface PermissionCallback {
    fun onPermissionGranted(perms: MutableList<String>)

    fun onPermissionDenied(perms: MutableList<String>)
}
