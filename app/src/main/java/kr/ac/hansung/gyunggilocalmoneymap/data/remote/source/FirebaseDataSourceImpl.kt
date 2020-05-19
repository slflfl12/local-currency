package kr.ac.hansung.gyunggilocalmoneymap.data.remote.source

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import kr.ac.hansung.gyunggilocalmoneymap.data.remote.model.LocalMapResponse.RegionMnyFacltStu.Place

class FirebaseDataSourceImpl : FirebaseDataSource {

    private val fireStore = FirebaseFirestore.getInstance()

    override fun savePlaces(list: List<Place>): Completable =

        Completable.create { emitter ->


            fireStore.collection(KEY_PLACES).document().set(list).addOnCompleteListener { task ->
                Log.d("aaaaa", "aaaaa")
                if (task.isSuccessful) {
                    emitter.onComplete()
                } else {
                    emitter.onError(Throwable(task.exception))
                }
            }
        }


    companion object {
        const val KEY_PLACES = "places"
    }
}