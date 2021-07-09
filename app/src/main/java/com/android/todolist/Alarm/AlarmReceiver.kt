package com.android.todolist.Alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        val alarmManager=context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(
            PendingIntent.getBroadcast(context,resultCode,
                Intent(context,AlarmReceiver::class.java),0))
        val i= Intent(context,PlayAlarmActivity::class.java)
        i.flags= Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(i)
    }
}