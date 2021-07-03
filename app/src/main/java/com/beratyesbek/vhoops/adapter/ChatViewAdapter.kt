package com.beratyesbek.vhoops.adapter

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.beratyesbek.vhoops.core.dataAccess.constants.ExtensionConstants
import com.beratyesbek.vhoops.core.utilities.animations.Animation
import com.beratyesbek.vhoops.core.utilities.control.CheckFirebaseUriType
import com.beratyesbek.vhoops.entities.concrete.dtos.ChatDto
import com.beratyesbek.vhoops.R
import com.beratyesbek.vhoops.viewUtilities.OnItemClickListener
import com.beratyesbek.vhoops.views.activities.ChatActivity
import com.beratyesbek.vhoops.views.fragment.ImageViewFragment
import com.beratyesbek.vhoops.views.fragment.VideoViewFragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatViewAdapter(
    val chatDtoList: ArrayList<ChatDto>,
    val itemClickListener: OnItemClickListener
) :
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
    private var viewLeftRightType = 0
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var context: Context
    private lateinit var userLocation: LatLng

    private  var itemPosition : Int? = null

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

            if (chatDtoList.get(position).senderId.equals(firebaseUser?.uid)) {
                viewLeftRightType = MSG_TYPE_RIGHT

                val view = layoutInflater.inflate(R.layout.chat_right_document_item, parent, false)
                return view
            }
            viewLeftRightType = MSG_TYPE_LEFT
            val view = layoutInflater.inflate(R.layout.chat_left_document_item, parent, false)
            return view

        } else if (type.equals(ExtensionConstants.IMAGE)) {
            viewTypeCheck = MSG_TYPE_IMAGE

            if (chatDtoList.get(position).senderId.equals(firebaseUser?.uid)) {
                viewLeftRightType = MSG_TYPE_RIGHT

                val view = layoutInflater.inflate(R.layout.chat_right_image_item, parent, false)
                return view
            }
            viewLeftRightType = MSG_TYPE_LEFT

            val view = layoutInflater.inflate(R.layout.chat_left_image_item, parent, false)
            return view

        } else if (type.equals(ExtensionConstants.MAP)) {
            viewTypeCheck = MSG_TYPE_MAP

            if (chatDtoList.get(position).senderId.equals(firebaseUser?.uid)) {
                viewLeftRightType = MSG_TYPE_RIGHT

                val view = layoutInflater.inflate(R.layout.chat_right_map_item, parent, false)
                return view
            }
            viewLeftRightType = MSG_TYPE_LEFT
            val view = layoutInflater.inflate(R.layout.chat_left_map_item, parent, false)
            return view

        } else if (type.equals(ExtensionConstants.VIDEO)) {
            viewTypeCheck = MSG_TYPE_VIDEO

            if (chatDtoList.get(position).senderId.equals(firebaseUser?.uid)) {
                viewLeftRightType = MSG_TYPE_RIGHT

                val view = layoutInflater.inflate(R.layout.chat_right_video_item, parent, false)
                return view
            }
            viewLeftRightType = MSG_TYPE_LEFT
            val view = layoutInflater.inflate(R.layout.chat_left_video_item, parent, false)
            return view

        } else if (type.equals(ExtensionConstants.AUDIO)) {
            viewTypeCheck = MSG_TYPE_AUDIO
            if (chatDtoList.get(position).senderId.equals(firebaseUser?.uid)) {
                viewLeftRightType = MSG_TYPE_RIGHT

                val view = layoutInflater.inflate(R.layout.chat_right_audio_item, parent, false)
                return view
            }
            viewLeftRightType = MSG_TYPE_LEFT
            val view = layoutInflater.inflate(R.layout.chat_left_audio_item, parent, false)
            return view

        } else {
            viewTypeCheck = MSG_TYPE_TEXT

            if (chatDtoList.get(position).senderId.equals(firebaseUser?.uid)) {
                viewLeftRightType = MSG_TYPE_RIGHT
                val view = layoutInflater.inflate(R.layout.chat_right_item, parent, false)
                return view
            }
            viewLeftRightType = MSG_TYPE_LEFT
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
            return e.toString()
        }
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {

        val chatDto = chatDtoList.get(position)

        try {
            if (viewTypeCheck == MSG_TYPE_IMAGE) {
                val uri = Uri.parse(chatDto.message.toString())
                Picasso.get().load(uri).into(holder.imageViewChat)

                holder.imageViewChat?.setOnClickListener {
                    displayImage(uri)
                }

            } else if (viewTypeCheck == MSG_TYPE_TEXT) {

                holder.textViewMessage?.text = chatDto.message.toString()

            } else if (viewTypeCheck == MSG_TYPE_MAP) {

                var map = HashMap<String, Number>()
                map = chatDto.message as HashMap<String, Number>
                userLocation = LatLng(map.get("latitude") as Double, map.get("longitude") as Double)
                holder.map?.setMapType(GoogleMap.MAP_TYPE_NONE);

            } else if (viewTypeCheck == MSG_TYPE_VIDEO) {

                val uri = Uri.parse(chatDto.message.toString())
                startVideo(uri, holder.videoView!!, holder.btnStartVideo!!)

            } else if (viewTypeCheck == MSG_TYPE_DOCUMENT) {

            } else if (viewTypeCheck == MSG_TYPE_AUDIO) {

                val uri = Uri.parse(chatDto.message.toString())

                manageMediaPlayer(
                    holder.seekBarAudio!!,
                    holder.btnPlayAudio!!,
                    holder.btnPauseAudio!!,
                    uri
                )

            }
            if (viewLeftRightType == MSG_TYPE_LEFT) {
                if (chatDtoList[0].userPicture != null && holder.imageViewUserProfile != null) {

                    Picasso.get().load(chatDtoList.get(0).userPicture)
                        .into(holder.imageViewUserProfile)

                }
            }

        } catch (e: Exception) {
            println(e.toString())
        }


    }

    private fun displayImage(uri: Uri) {
        val transaction = (context as ChatActivity).supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.fade_in_anim, R.anim.fade_out)
        transaction.replace(R.id.frameLayout_chat, ImageViewFragment(uri))
        transaction.commit()
    }

    private fun startVideo(uri: Uri, videoView: VideoView, btnStart: ImageButton) {

        videoView.setVideoURI(uri)
        videoView.seekTo(1)
        btnStart.setOnClickListener {
            val transaction = (context as ChatActivity).supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.fade_in_anim, R.anim.slide_out_anim)
            transaction.replace(R.id.frameLayout_chat, VideoViewFragment(uri))
            transaction.commit()
        }
    }


    private fun manageMediaPlayer(
        seekBar: SeekBar,
        btnStart: ImageButton,
        btnStop: ImageButton,
        uri: Uri
    ) {
        val mediaPlayer = MediaPlayer.create(context, uri)
        btnStart.setOnClickListener {
            initialiseSeekBar(seekBar, mediaPlayer, btnStart, btnStop)
            mediaPlayer.start()
            Animation.hideAnim(btnStart)
            Animation.revealAnim(btnStop)

        }
        btnStop.setOnClickListener {
            Animation.hideAnim(btnStop)
            Animation.revealAnim(btnStart)
            mediaPlayer.pause()
        }
    }

    private fun initialiseSeekBar(
        seekBar: SeekBar,
        mediaPlayer: MediaPlayer,
        btnStart: ImageButton,
        btnStop: ImageButton
    ) {

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer?.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                Animation.hideAnim(btnStop)
                Animation.revealAnim(btnStart)
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })

        seekBar.max = mediaPlayer.duration


        val handler = android.os.Handler()
        var control = false
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    seekBar.progress = mediaPlayer.currentPosition
                    if (seekBar.progress == mediaPlayer.duration && !control) {
                        Animation.hideAnim(btnStop)
                        Animation.revealAnim(btnStart)
                        control = true
                    }
                    handler.postDelayed(this, 1000)
                } catch (e: Exception) {
                    seekBar.progress = 0
                }

            }

        }, 0)

    }


    override fun getItemCount(): Int {
        return chatDtoList.size
    }

    inner class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view), OnMapReadyCallback {

        val textViewMessage: TextView?
        val imageViewUserProfile: CircleImageView?
        val imageViewChat: ImageView?
        val videoView: VideoView?
        var mapView: MapView?
        var map: GoogleMap? = null
        val btnStartVideo: ImageButton?
        val btnPlayAudio: ImageButton?
        val btnPauseAudio: ImageButton?
        val seekBarAudio: SeekBar?
        val relativeLayout : RelativeLayout?


        init {

            textViewMessage = view.findViewById(R.id.textView_show_messages)
            imageViewChat = view.findViewById(R.id.imageView_chat)
            imageViewUserProfile = view.findViewById(R.id.imageView_show_message_ProfileImage)
            videoView = view.findViewById(R.id.videoView_chat)
            btnPlayAudio = view.findViewById(R.id.btn_play_audio)
            btnPauseAudio = view.findViewById(R.id.btn_stop_audio)
            btnStartVideo = view.findViewById(R.id.btn_start_video)
            seekBarAudio = view.findViewById(R.id.seekBar_audio)
            mapView = view.findViewById(R.id.map_chat)
            relativeLayout = view.findViewById(R.id.chat_item_relativeLayout)
          /*  if (relativeLayout != null){
                relativeLayout.setOnLongClickListener(this)
                relativeLayout.setOnClickListener(this)
            }*/





            if (mapView != null) {
                mapView!!.onCreate(null);
                mapView!!.onResume();
                mapView!!.getMapAsync(this);
            }



        }



        override fun onMapReady(googleMap: GoogleMap?) {
            MapsInitializer.initialize(context);
            map = googleMap
            map!!.addMarker(MarkerOptions().position(userLocation).title("Location"))
            map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15f))
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationListener = object : LocationListener {

                override fun onLocationChanged(location: Location) {

                }




            }
            map?.setOnMapLongClickListener {
            }

        }

/*
        override fun onLongClick(p0: View?): Boolean {
            val position = adapterPosition
            relativeLayout?.setBackgroundResource(R.drawable.oppocity_background)
            if (position != RecyclerView.NO_POSITION) {

                itemClickListener.onItemLongClick(position)
                return true
            }

            return false
        }

        override fun onClick(p0: View?) {

            val position = adapterPosition
            relativeLayout?.setBackgroundResource(0)
            if (position != RecyclerView.NO_POSITION) {
                itemClickListener.onItemClick(position)
            }

        }
*/

    }



}


