package com.demo.ubike.ui.fragment

import androidx.databinding.ViewDataBinding
import com.demo.ubike.R
import com.demo.ubike.data.viewmodel.BaseViewModel
import com.demo.ubike.extension.permission.PermissionCallback
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest

abstract class BasePermissionFragment<T : ViewDataBinding, V : BaseViewModel> :
    BaseFragment<T, V>(), EasyPermissions.PermissionCallbacks {

    private var mPermissionCallback: PermissionCallback? = null
    private var mRequestCode: Int? = null

    protected fun requestPermissions(
        vararg permissions: String,
        rationale: String?,
        requestCode: Int,
        permissionCallback: PermissionCallback
    ) {
        mPermissionCallback = permissionCallback
        mRequestCode = requestCode
        if (hasPermissions(*permissions)) {
            onPermissionsGranted(requestCode, mutableListOf(*permissions))
        } else {
            val request = PermissionRequest.Builder(this, requestCode, *permissions)
                .setRationale(R.string.location_permission_hint)
                .setPositiveButtonText(R.string.setting)
                .setNegativeButtonText(R.string.cancel)
                .setTheme(android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)
                .build()

            EasyPermissions.requestPermissions(request)
        }
    }

    protected fun hasPermissions(vararg permissions: String): Boolean {
        return EasyPermissions.hasPermissions(requireContext(), *permissions)
    }

    @Suppress("DEPRECATION")
    final override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    final override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == mRequestCode) {
            mPermissionCallback?.onPermissionGranted(perms)
        }
    }

    final override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == mRequestCode) {
            mPermissionCallback?.onPermissionDenied(perms)
        }
    }
}