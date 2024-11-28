import com.example.submission1intermediate.model.LoginRequest
import com.example.submission1intermediate.model.LoginResponse
import com.example.submission1intermediate.model.RegisterRequest
import com.example.submission1intermediate.model.RegisterResponse
import com.example.submission1intermediate.model.Story
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<RegisterResponse>

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int
    ): Response<List<Story>>
}
