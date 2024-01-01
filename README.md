# breakdownassist
# Assignment2

## Damien Driver - 20099851

---

This is and android studio / kotlin based project.

The idea for the app is that the location of local breakdown services and garages can be listed and displayed on a map. A user will be able to record their location and enter contact details and images that may help assist them on the roadside.

### App Functionality

Login Support is provided through Firebase Authentication, users can create an account either using their own email/password combination or through Google Sign In.

MVVM design approach was used in this version of the app. A Nav drawer was introduced and fragments used for creating views within the app.

A new branch was created in github for Assignment2. The new branch has not yet been merged with main. History of commits with messages. 

Data persistence is supported through Firebase Realtime Database and profile images are managed in Firebase Cloud Storage.

Garage details can be edited/deleted using swipe functions. With toggle button allowing all users listings to be viewed.

#### Personal Statement
I found Assignment 2 to be a tough challenge. My initial thought was to keep my original app and build the new features on top. This didn't work out and I had to revert to following the labs more closely. In hindsight I should have converted my first app to MVVM design straight away and then bolted on the new features. Due to time constraints I had to omit the maps feature from assignment2. I feel that while the additions of the login support, nav drawer and swipe functions are excellent advances the missing map interaction is disappointing. This is something I will work to adding in my own time.

##### User Nav

![User Nav Flow](app/src/main/res/drawable/breakdown_Nav.png)