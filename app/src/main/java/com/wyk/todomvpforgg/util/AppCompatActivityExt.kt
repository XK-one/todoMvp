package com.wyk.todomvpforgg.util

import androidx.annotation.IdRes
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

fun AppCompatActivity.replaceFragmentInActivity(fragment: Fragment, @IdRes frameId: Int){
    supportFragmentManager.transact {
        replace(frameId, fragment)
    }
}

fun AppCompatActivity.setupActionBar(@IdRes idRes: Int, action: ActionBar.() -> Unit){
    setSupportActionBar(findViewById(idRes))
    supportActionBar?.run{
        action()
    }
}

private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit){
    beginTransaction().apply {
        action()
    }.commit()
}