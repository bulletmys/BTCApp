import com.bulletmys.bitcoin.Interface.RetrofitServices
import com.bulletmys.bitcoin.models.Response
import io.reactivex.Observable
import retrofit2.Call

class CryptoRepository(val apiService: RetrofitServices) {
    fun getCryptoInfo(from: String, to: String, limit: Int): Observable<Response> {
        return apiService.search(queryFrom = from, queryTo = to, limit = limit)
    }
}
