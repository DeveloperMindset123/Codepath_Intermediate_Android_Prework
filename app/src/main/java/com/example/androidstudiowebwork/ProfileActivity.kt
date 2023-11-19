package com.example.androidstudiowebwork

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {  //declare the activity based on the name of the file
    //initialize variable
    lateinit var ivImage: ImageView
    lateinit var tvName: TextView
    lateinit var btLogout: Button
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)  //to render the UI defined in activity_profile.xml
        //Assign variable
        ivImage = findViewById(R.id.iv_image)
        tvName = findViewById(R.id.tv_name)
        btLogout = findViewById(R.id.bt_logout)

        //initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //intiialize firebase user
        val firebaseUser = firebaseAuth.currentUser  //returns the currently signed-in firebase user

        //check condition
        if (firebaseUser != null) {
            //when firebase user is not equal to null set image to image view
            Glide.with(this@ProfileActivity).load(firebaseUser.photoUrl).into(ivImage)  //will display the pfp associated with the correspoding google account
            //set name on text view
            tvName.text = firebaseUser.displayName  //will display the name of the corresponding user
        }

        //initialize sign in clinet
        googleSignInClient = GoogleSignIn.getClient(this@ProfileActivity, GoogleSignInOptions.DEFAULT_SIGN_IN)
        btLogout.setOnClickListener{
            //sign out from google
            googleSignInClient.signOut().addOnCompleteListener{task ->
                //check condition
                if (task.isSuccessful) {  //indicates user has successfully signed out
                    firebaseAuth.signOut()
                    //display toast
                    Toast.makeText(applicationContext, "Logout Successful", Toast.LENGTH_SHORT).show()
                    //finish activity
                    finish()
                }
            }
        }
    }
}

/* --> old default code
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.androidstudiowebwork.ui.theme.AndroidStudioWebworkTheme

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidStudioWebworkTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidStudioWebworkTheme {
        Greeting("Android")
    }
} */