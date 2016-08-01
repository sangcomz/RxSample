package co.riiid.rxsample

import android.view.View
import rx.Observable
import rx.Subscriber
import rx.android.MainThreadSubscription

/**
 * Created by sangcomz on 6/28/16.
 */
class ViewClickOnSubscribe(val view: View) : Observable.OnSubscribe<View> {

    override fun call(subscriber: Subscriber<in View>) {
        rx.android.MainThreadSubscription.verifyMainThread()

        val listener = View.OnClickListener {
            if (!subscriber.isUnsubscribed) {
                subscriber.onNext(view)
            }
        }
        view.setOnClickListener(listener)

        subscriber.add(object : MainThreadSubscription() {
            override fun onUnsubscribe() {
                view.setOnClickListener(null)
            }
        })
    }
}
