package com.example.androidteststudyguide.features.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.work.*
import com.example.androidteststudyguide.databinding.FragmentWorkmanagerBinding
import com.example.androidteststudyguide.features.core.workers.NotificationWorker
import com.example.androidteststudyguide.features.core.workers.RingtoneWorker
import java.util.concurrent.TimeUnit


class WorkManagerFragment: Fragment() {

    private val viewModel: WorkManagerViewModel by viewModels {
        WorkManagerViewModel.WorkManagerViewModelFactory(
            application = requireActivity().application
        )
    }
    private lateinit var binding: FragmentWorkmanagerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkmanagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btn1.setOnClickListener {
            startOnetimeWork()
        }

        binding.btn2.setOnClickListener {
            startPeriodicWork()
        }

        binding.btn3.setOnClickListener {
            chainWork()
        }

        binding.btn4.setOnClickListener {
            startObservableWork()
        }

        binding.btn5.setOnClickListener {
            startCancelableWork()
        }

        binding.btn6.setOnClickListener{
            cancelWork()
        }

        viewModel.statusLiveData1.observe(viewLifecycleOwner){ listOfWorkInfo ->

            // Note that these next few lines grab a single WorkInfo if it exists
            // This code could be in a Transformation in the ViewModel; they are included here
            // so that the entire process of displaying a WorkInfo is in one location.

            // If there are no matching work info, do nothing
            if (listOfWorkInfo.isNullOrEmpty()) {
                binding.statusText.text = "isNullOrEmpty"
                return@observe
            }

            val list = mutableListOf<String>()
            listOfWorkInfo.forEach {
                list.add("${it.id} ${it.state}")
            }

            binding.statusText.text = list.toString()
        }

        viewModel.statusLiveData2.observe(viewLifecycleOwner){ listOfWorkInfo ->

            // Note that these next few lines grab a single WorkInfo if it exists
            // This code could be in a Transformation in the ViewModel; they are included here
            // so that the entire process of displaying a WorkInfo is in one location.

            // If there are no matching work info, do nothing
            if (listOfWorkInfo.isNullOrEmpty()) {
                binding.statusText2.text = "isNullOrEmpty"
                return@observe
            }

            val list = mutableListOf<String>()
            listOfWorkInfo.forEach {
                list.add("${it.id} ${it.state}")
            }

            binding.statusText2.text = list.toString()
        }
    }



    // most simple version
    fun startOnetimeWork(){
        // Step 1: get work manager
        val workManager = WorkManager.getInstance(requireActivity().applicationContext)
        workManager.enqueue(OneTimeWorkRequest.from(RingtoneWorker::class.java))
    }



    fun startPeriodicWork() {
        // Step 1: get work manager
        val workManager = WorkManager.getInstance(requireActivity().applicationContext)

        // step 2: create request
        val requestBuilder = PeriodicWorkRequestBuilder<NotificationWorker>(15, TimeUnit.MINUTES)


        // step3: create Data
        val dataBuilder: Data.Builder = Data.Builder()
        dataBuilder.putString("title", "example startPeriodicWork")
        dataBuilder.putString("content", "if you see this in 15min periodic work success")
        val data: Data = dataBuilder.build()

        requestBuilder.setInputData(data)

        // or if no input needed
//        val request: PeriodicWorkRequest = PeriodicWorkRequest.Builder(NotificationWorker::class.java, 15, TimeUnit.MINUTES)
//            .build()
        workManager.enqueue(requestBuilder.build())
    }


    /**
     * Instead of calling workManager.enqueue(), call workManager.beginWith().
     * This returns a WorkContinuation, which defines a chain of WorkRequests.
     * You can add to this chain of work requests by calling then() method
     */
    fun chainWork(){
        val workManager = WorkManager.getInstance(requireActivity().applicationContext)

        // first begin with a task
        var continuation = workManager.beginWith(OneTimeWorkRequest
                .from(RingtoneWorker::class.java))


        // create second task's request

        val requestBuilder = OneTimeWorkRequestBuilder<NotificationWorker>()

        // create Data
        val dataBuilder: Data.Builder = Data.Builder()
        dataBuilder.putString("title", "example chainWork")
        dataBuilder.putString("content", "displaying notification after the ringtone")
        val data = dataBuilder.build()

        requestBuilder.setInputData(data)

        val request = requestBuilder.build()

        // add second task to continuation
        continuation = continuation.then(request)


        // start work
        continuation.enqueue()
    }


    /*
        Everything is the same except create continuation with 'beginUniqueWork'
        Using uniqueWorkNam: This names the entire chain of work requests so that you can refer to and query them together.

        ExistingWorkPolicy options are REPLACE, KEEP or APPEND.
        https://developer.android.com/reference/androidx/work/ExistingWorkPolicy#replace
     */
    fun chainWorkEnsureUnique(){
        val workManager = WorkManager.getInstance(requireActivity().applicationContext)

        // first begin with a task
        var continuation = workManager.beginUniqueWork(
             "image_manipulation_work",
             ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequest.from(RingtoneWorker::class.java))


        // create second task's request

        val requestBuilder = OneTimeWorkRequestBuilder<NotificationWorker>()

        // create Data
        val dataBuilder: Data.Builder = Data.Builder()
        dataBuilder.putString("title", "example chainWork")
        dataBuilder.putString("content", "displaying notification after the ringtone")
        val data = dataBuilder.build()

        requestBuilder.setInputData(data)

        val request = requestBuilder.build()

        // add second task to continuation
        continuation = continuation.then(request)


        // start work
        continuation.enqueue()
    }


    /*
    Add tag to be able to get the livedata
    https://developer.android.com/codelabs/android-workmanager#7
     */
    fun startObservableWork(){

        // Step 1: get work manager
        val workManager = viewModel.workManager

        // step 2: create request
        val requestBuilder = OneTimeWorkRequestBuilder<NotificationWorker>()

        requestBuilder.addTag("TAG_FOR_LIVEDATA") // <-- TAG must match in viewModel

        // step3: create Data
        val dataBuilder: Data.Builder = Data.Builder()
        dataBuilder.putString("title", "example startObservableWork")
        dataBuilder.putString("content", "status should have updated")
        dataBuilder.putLong("sleepTime", 3000)
        val data = dataBuilder.build()

        requestBuilder.setInputData(data)

        workManager.enqueue(requestBuilder.build())

    }


    /*
        Only able to cancel unique work
     */
    fun startCancelableWork(){

        val workManager = WorkManager.getInstance(requireActivity().applicationContext)

        // first begin with a task
        var continuation = workManager.beginUniqueWork(
            "CANCELABLE_UNIQUE_WORK",  // workName must match in the viewModel when calling cancel
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequest.from(RingtoneWorker::class.java))


        // create second task's request

        val requestBuilder = OneTimeWorkRequestBuilder<NotificationWorker>()
        requestBuilder.addTag("CANCELABLE_UNIQUE_WORK") // <-- TAG must match in viewModel

        // create Data
        val dataBuilder: Data.Builder = Data.Builder()
        dataBuilder.putString("title", "example startCancelableWork")
        dataBuilder.putString("content", "this is a long task, cancel it in the middle")
        dataBuilder.putLong("sleepTime", 600000)
        val data = dataBuilder.build()
        Toast.makeText(requireContext(), "This is a long task, cancel it", Toast.LENGTH_SHORT).show()

        requestBuilder.setInputData(data)

        val request = requestBuilder.build()

        // add second task to continuation
        continuation = continuation.then(request)


        // start work
        continuation.enqueue()
    }
    fun cancelWork(){
        viewModel.cancelWork()
    }

    /**
     *
     *  Lastly there is also constraint we can add, meaning only start work when constraint are meet
     *  https://developer.android.com/codelabs/android-workmanager#10
     */

}