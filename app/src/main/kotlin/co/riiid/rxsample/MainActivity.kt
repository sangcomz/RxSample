package co.riiid.rxsample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import rx.Observable
import rx.Subscriber
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    var subscriber: Subscriber<View>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initButton()
        subscriber = getChoiceSubcription()

        val ob1 = Observable.just("a")
        val ob2 = Observable.just("b")
        val ob3 = Observable.just("c")
        val ob4 = Observable.just("d")
        val ob5 = Observable.just("e")

        println(convertQuestionsNumberRange("Questions 112-533 refer to the following form and e-mail.", 10, 20))


        val obs: ArrayList<Observable<String>> = arrayListOf()
        obs.add(ob1)
        obs.add(ob2)
        obs.add(ob3)
        obs.add(ob4)
        obs.add(ob5)
        val type = "toeic-part7"
        val clean1 = type.replace("toeic-part", "")


//        println("type.replace() ::::: ${type.replace("\\D".toRegex(), "")}")
//        println("type.replace() ::::: ${type.replace("[^0-9]".toRegex(), "")}")
//        println("clean1 ::::: ${clean1}")

//        while (type.contains("[^0-9]".toRegex())) {
//            text = text.rep("\\^+".toRegex(), "$space${num++}$space")
//        }

//
        Observable.zip(obs, {
            val array = arrayListOf<String>()
            it.forEach {
                println(it)
                if (it is String)
                    array.add(it)
            }
            it
        }).subscribe {
            it.forEach {
                println("zip ::: " + it)
            }
        }
//
        val strs = arrayListOf<String>()
        Observable
                .merge(obs)
                .subscribe {
                    println("merge :::: $it")
                    strs.add(it)
                }

        println("strs.szie ::: ${strs.size}")
    }

    fun initButton() {
        btn1.setOnClickListener(this)
        btn2.setOnClickListener(this)
        btn3.setOnClickListener(this)
        btn4.setOnClickListener(this)
    }

    fun convertQuestionsNumberRange(intro: String, start: Int, end: Int): String
            = intro.replace("([0-9]+)-([0-9]+)".toRegex(), "$start-$end")


    fun getChoiceSubcription(): Subscriber<View> {
        return object : Subscriber<View>() {
            var count = 0
            override fun onNext(t: View?) {
                println("view ::: $t")
                count++
                println("count $count")
            }

            override fun onError(e: Throwable?) {
            }

            override fun onCompleted() {
            }

        }
    }

    override fun onClick(p0: View?) {
        Observable.create<View> { subscriber ->
            subscriber.onNext(p0)
        }.subscribe(subscriber)
    }

}
