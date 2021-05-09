package com.beratyesbek.Vhoops.Adapter

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.beratyesbek.Vhoops.Core.DataAccess.Constants.ExtensionConstants
import com.beratyesbek.Vhoops.Core.Utilities.Control.CheckFirebaseUriType
import com.beratyesbek.Vhoops.Entities.Concrete.Dtos.ChatDto
import com.beratyesbek.Vhoops.Entities.Concrete.User
import com.beratyesbek.Vhoops.R
import com.beratyesbek.Vhoops.Views.Activities.ChatActivity
import com.beratyesbek.Vhoops.Views.Fragment.VideoViewFragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatViewAdapter(val chatDtoList: ArrayList<ChatDto>) :
    RecyclerView.Adapter<ChatViewAdapter.ChatViewHolder>() {

    private val MSG_TYPE_RIGHT = 0
    private val MSG_TYPE_LEFT = 1
    private val MSG_TYPE_IMAGE = 2
    private val MSG_TYPE_VIDEO = 3
    private val MSG_TYPE_DOCUMENT = 4
    private val MSG_TYPE_AUDIO = 5
    private val MSG_TYPE_MAP = 6
    private val MSG_TYPE_TEXT = 7

    private var viewTypeCheck = 0
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var context: Context
    private lateinit var userLocation: LatLng

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val view = getView(viewType, layoutInflater, parent)
        return ChatViewHolder(view)

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun getView(position: Int, layoutInflater: LayoutInflater, parent: ViewGroup): View {

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val type = checkMessageType(position)

        if (type.equals(ExtensionConstants.DOCUMENT)) {
            viewTypeCheck = MSG_TYPE_DOCUMENT

            if (chatDtoList.get(position).senderId.equals(firebaseUser.uid)) {
                val view = layoutInflater.inflate(R.layout.chat_right_document_item, parent, false)
                return view
            }

            val view = layoutInflater.inflate(R.layout.chat_left_document_item, parent, false)
            return view

        } else if (type.equals(ExtensionConstants.IMAGE)) {
            viewTypeCheck = MSG_TYPE_IMAGE

            if (chatDtoList.get(position).senderId.equals(firebaseUser.uid)) {
                val view = layoutInflater.inflate(R.layout.chat_right_image_item, parent, false)
                return view
            }
            val view = layoutInflater.inflate(R.layout.chat_left_image_item, parent, false)
            return view

        } else if (type.equals(ExtensionConstants.MAP)) {
            viewTypeCheck = MSG_TYPE_MAP

            if (chatDtoList.get(position).senderId.equals(firebaseUser.uid)) {
                val view = layoutInflater.inflate(R.layout.chat_right_map_item, parent, false)
                return view
            }
            val view = layoutInflater.inflate(R.layout.chat_left_map_item, parent, false)
            return view

        } else if (type.equals(ExtensionConstants.VIDEO)) {
            viewTypeCheck = MSG_TYPE_VIDEO

            if (chatDtoList.get(position).senderId.equals(firebaseUser.uid)) {
                val view = layoutInflater.inflate(R.layout.chat_right_video_item, parent, false)
                return view
            }
            val view = layoutInflater.inflate(R.layout.chat_left_video_item, parent, false)
            return view

        } else {
            viewTypeCheck = MSG_TYPE_TEXT

            if (chatDtoList.get(position).senderId.equals(firebaseUser.uid)) {
                val view = layoutInflater.inflate(R.layout.chat_right_item, parent, false)
                return view
            }
            val view = layoutInflater.inflate(R.layout.chat_left_item, parent, false)
            return view
        }

    }

    fun checkMessageType(position: Int): String {
        try {
            val message = chatDtoList.get(position).message.toString()
            if (chatDtoList.get(position).message is Map<*, *>) {
                return ExtensionConstants.MAP
            } else {
                val type = CheckFirebaseUriType.checkUriType(message)
                if (type == null) {
                    return ExtensionConstants.TEXT
                }
                return type

            }

        } catch (e: Exception) {
            println(e.toString())
            return e.toString()
        }
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        println(MSG_TYPE_VIDEO)
        println("type " + viewTypeCheck)

        val chatDto = chatDtoList.get(position)

        if (viewTypeCheck == MSG_TYPE_IMAGE) {
            val uri = Uri.parse(chatDto.message.toString())
            Picasso.get().load(uri).into(holder.imageView_chat)
        }
        else if (viewTypeCheck == MSG_TYPE_TEXT) {
            holder.textViewMessage!!.text = chatDto.message.toString()

        }
        else if (viewTypeCheck == MSG_TYPE_MAP) {
            var map = HashMap<String, Number>()
            map = chatDto.message as HashMap<String, Number>
            userLocation = LatLng(map.get("latitude") as Double, map.get("longitude") as Double)
        }
        else if (viewTypeCheck == MSG_TYPE_VIDEO) {
            val uri = Uri.parse(chatDto.message.toString())
            startVideo(uri,holder.videoView!!,holder.btnStartVideo!!)
        }
        else if(viewTypeCheck == MSG_TYPE_DOCUMENT){

        }
        if (viewTypeCheck == MSG_TYPE_LEFT) {
            if (chatDtoList.get(0).userPicture != null) {
                Picasso.get().load(chatDto.userPicture).into(holder.imageViewUserProfile)
            }
        }
    }

    fun startVideo(uri : Uri,videoView: VideoView,btnStart : ImageButton){

        videoView.setVideoURI(uri)
        videoView.seekTo(1)
        btnStart.setOnClickListener {
        //    val fragment : Fragment? = (context as ChatActivity).supportFragmentManager.findFragmentById(R.layout.fragment_video_view!!)
            val transaction = (context as ChatActivity).supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.zoom_in,R.anim.zoom_out)
            transaction.replace(R.id.frameLayout_chat,VideoViewFragment(uri))
            transaction.commit()
        }
    }


    override fun getItemCount(): Int {
        return chatDtoList.size
    }

    inner class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view), OnMapReadyCallback {

        val textViewMessage: TextView?
        val imageViewUserProfile: ImageView?
        val imageView_chat: ImageView?
        val videoView: VideoView?
        var mapView: GoogleMap? = null
        val btnStartVideo : ImageButton?


        init {

            textViewMessage = view.findViewById(R.id.textView_show_messages)
            imageView_chat = view.findViewById(R.id.imageView_chat)
            imageViewUserProfile = view.findViewById(R.id.imageView_show_message_ProfileImage)
            videoView = view.findViewById(R.id.videoView_chat)
            btnStartVideo = view.findViewById(R.id.btn_start_video)
            val mapFragment = (context as ChatActivity).supportFragmentManager
                .findFragmentById(R.id.map_chat) as SupportMapFragment?

            if(mapFragment != null){
                mapFragment.getMapAsync(this)

            }

        }

        override fun onMapReady(googleMap: GoogleMap?) {
            mapView = googleMap
            mapView!!.addMarker(MarkerOptions().position(userLocation).title("Your last Location"))
            mapView!!.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationListener = object : LocationListener {

                override fun onLocationChanged(location: Location) {

                }


            }

        }


    }


}