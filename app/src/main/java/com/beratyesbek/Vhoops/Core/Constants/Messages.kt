package com.beratyesbek.Vhoops.Core.Constants

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
    }
}