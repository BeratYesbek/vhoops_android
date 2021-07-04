# vhoops_android

                                      #Welcome To Vhoops
####Description
Vhoops developed with android Sdk and Kotlin. What can you do in Vhoops. You can chat with your friends. You can share location, image,file video or sound recording. Also you are able to customize your profile and your groups chat. 


- message
- group chat
- share location,file sound recording,image,video
- customize your profile
- add your friends

###Languages and Tools

    ###### Firebase,Android,Kotlin,Dagger Hilt, Mvvm,Jetpack,JitsiMeet,Retrofit

While developing Vhoops was used Kotlin, Firebase,Dagger Hilt, JitsiMeet,Mvvm, Jetpack and Retrofit. Also Vhoops has a SOLID coding technic, so I tried to obey the object-oriented programming (OOP). It has a couple of missing. I tried to completed everything on the app, but sometimes I may have forgotten a couple of features of, while I was Coding Vhoops. There is a business, dataAccess, entities, core and the other one packages.
 
 #####Business package </br>
 Business package is my business rules package I code in here my app rules.
        for example password cannot be less than 8 characters 
        
DataAccess Package</br>
  I used a diffrent purpose to DataAccess package . For example I would like to get data details if I do that in the core/dataAccess packages dto object (detail objects) can mixed. I only used database object in core/data access layer so If I would like to get Details I should use  this layer If I would like to get Single data I should use core/dataAccess layer. In this technic I tried to blocked mixing dto object and data access object
 
 #####Entities Package </br>
In Entities package, exist my database objects and Dtos

######Core Package</br>
In Core package, I managed firebase operations. For example, I would like to add, update, delete  user, I should do that in this layer. The purpose of the layer save mixing database object and dto object. dto is not database object

