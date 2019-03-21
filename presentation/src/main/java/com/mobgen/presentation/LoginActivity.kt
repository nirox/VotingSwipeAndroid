package com.mobgen.presentation

import android.os.Bundle
import android.util.Log
import com.mobgen.domain.subscribe
import com.mobgen.domain.useCase.Authenticate
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class LoginActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var authenticate: Authenticate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_main)

        /*val authenticate =
            Authenticate(UserRepositoryImpl(UserDataMapperReverse(), UserDataMapper(), FirebaseDataResource()))
        val register = Register(UserRepositoryImpl(UserDataMapperReverse(), UserDataMapper(), FirebaseDataResource()))
        val auth = GetAuthUser(UserRepositoryImpl(UserDataMapperReverse(), UserDataMapper(), FirebaseDataResource()))*/
        authenticate.execute(
            "pepito@gmail.com",
            "test1pass"
        ).subscribe(
            executor = AndroidSchedulers.mainThread(),
            onSuccess = {
                Log.v("mio", "auth")
                /*auth.execute().subscribe(
                    executor = AndroidSchedulers.mainThread(),
                    onSuccess = {
                        Log.v("mio", "auth")

                    },
                    onError = {
                        Log.v("mio", "not auth")
                    }
                )*/

            },
            onError = {
                Log.v("mio", "not auth")
            }
        )


    }


}
