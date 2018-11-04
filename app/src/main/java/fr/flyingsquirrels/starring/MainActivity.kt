package fr.flyingsquirrels.starring

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import fr.flyingsquirrels.starring.search.SearchInterface
import fr.flyingsquirrels.starring.search.viewmodel.SearchViewModel
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    companion object {
        const val SELECTED_TAB = "selected"
    }

    val disposables = CompositeDisposable()

    private lateinit var navController: NavController

    private val searchViewModel: SearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        navController = findNavController(R.id.nav_host_fragment)
        nav.setupWithNavController(navController)

        //setupActionBarWithNavController(navController)

        ViewCompat.setTransitionName(nav, BaseDetailActivity.EXTRA_SHARED_NAV)
        intent?.extras?.getInt(SELECTED_TAB)?.let {
            nav.selectedItemId = it
            intent.extras?.clear()
        }
    }

    override fun onBackPressed() {
        if(nav.selectedItemId == R.id.movies){
            finishAffinity()
        }
        super.onBackPressed()
    }

    override fun onSupportNavigateUp() =
            navController.navigateUp()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView

        // Set up the query listener that executes the search
        Observable.create(ObservableOnSubscribe<String> { subscriber ->
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    subscriber.onNext(newText!!)
                    return false
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    subscriber.onNext(query!!)
                    return false
                }
            })
        }).map { text -> text.toLowerCase().trim() }
        .debounce(500, TimeUnit.MILLISECONDS)
        .distinctUntilChanged()
        .filter { text -> text.isNotBlank() }
        .subscribe { text ->
            Timber.d( "subscriber: $text")
            if (navController.currentDestination?.id != R.id.search) {
                val args = Bundle().apply { putString(SearchInterface.QUERY,text) }
                navController.navigate(R.id.search, args)
            }else {
                searchViewModel.query.onNext(text)
            }
        }.let{
            disposables.add(it)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

}

