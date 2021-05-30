package com.beratyesbek.vhoops.Core.Constants

open class Messages {
    companion object {

        val USER_CREATED_MESSAGE: String = "Hesabınız başarılı bir şekilde oluşturuldu."
        val INVALID_USERNAME_MESSAGE: String = "Kullanıcı adı geçersiz karekterler içermektedir";
        val USERNAME_EXISTS_MESSAGE: String = "Kullanıcı adı mevcut";
        val INVALID_LENGTH_PASSWORD_MESSAGE: String = "Şifreniz mininimum 8 karekterden oluşmalıdır";
        val INVALID_CHARACTER_PASSWORD_MESSAGE: String = "Şifreniz en az bir adet büyük harf ve özel karekter içermelidir. Örnek = Vhoops123?"
        val INVALID_FIRSTNAME_MESSAGE: String = "Adınız 3 karekterden küçük olamaz"
        val INVALID_LASTNAME_MESSAGE: String = "Soyadınız 3 karekterden küçük olamaz"
        val INVALID_EMAIL_MESSAGE: String = "Geçersiz e-posta adresi"
        val EMAIL_EXISTS_MESSAGE: String = "E-posta adresi mevcut";
        val GET_DATA_SUCCESS : String = "Veri getirme işlemi başarılı"
        val SUCCESS_UPDATE_USER_DATA : String = "Güncelleme tamamlandı";
        val FAILED_UPDATE_USER_DATA : String = "Güncelleme tamamlandı";
        val USER_PHOTO_UPLOADED : String = "Fotoğraf eklendi"
        val USER_PHOTO_UPLOAD_FAILED : String = "Fotoğraf eklenemedi"
        val USER_NAME_UPDATED : String = "Kullanıcı güncellendi."
        val USER_NAME_UPDATE_FAILED : String = "Kullanıcı adı güncelle sırasında bir hata oluştu."
        val LOCATION_FAILED : String = "Konumunuz alınamadı tekrar deneyiniz."
    }
}