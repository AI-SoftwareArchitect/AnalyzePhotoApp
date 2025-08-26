📸 Photo Editor App
Photo Editor App, kullanıcıların fotoğraf çekmesini, düzenlemesini ve organize etmesini sağlayan modern bir Android uygulamasıdır. Artırılmış gerçeklik (AR) destekli canlı filtreler, gelişmiş düzenleme modları ve sezgisel bir kullanıcı arayüzü ile fotoğrafçılık deneyimini bir üst seviyeye taşır.

✨ Temel Özellikler
Siyah & Altın Tema: Şık, minimalist ve göz yormayan, tamamen siyah ve altın renklerinden oluşan bir arayüz. Butonlar ve diğer interaktif elemanlarda altın rengi, gradyanlar ve akıcı animasyonlar kullanılarak modern bir estetik sağlanmıştır.

AR Destekli Filtreler: Kamera ekranında canlı olarak köpek ve kedi gibi eğlenceli AR filtrelerini yüzünüze uygulayın.

Gelişmiş Fotoğraf Düzenleme: Çekilen fotoğrafları CleanMode ve FixMode gibi özel modlarla profesyonelce düzenleyin.

MVI Mimarisi: Uygulama, temiz, ölçeklenebilir ve test edilebilir bir yapı sunan Model-View-Intent (MVI) mimarisi üzerine inşa edilmiştir.

Kotlin & Jetpack Compose: Tamamen Kotlin dilinde ve modern deklaratif UI araç seti olan Jetpack Compose ile geliştirilmiştir.

🛠️ Mimari ve Teknolojiler
Bu proje, temiz mimari (Clean Architecture) ilkelerine bağlı kalarak katmanlı bir yapıda tasarlanmıştır. Her katmanın belirli bir sorumluluğu vardır, bu da kodu yönetmeyi, test etmeyi ve bakımını yapmayı kolaylaştırır.

Mimarinin Katmanları:
Presentation Katmanı: Kullanıcı arayüzünü (UI) yönetir. MVI (Model-View-Intent) mimarisiyle, kullanıcı etkileşimleri (Intent), UI durumları (State) ve veriler (Model) arasında tek yönlü bir veri akışı sağlanır. Her ekranın kendine ait bir ViewModel'ı, Intent'i ve State'i bulunur.

Domain Katmanı: İş mantığını içerir. Platformdan bağımsızdır. Use Case'ler, iş kurallarını tanımlar ve Repository'lerden gelen veriyi işler.

Data Katmanı: Veri kaynaklarından (hem yerel hem de uzak) veriyi yönetir. Bu katman, Repository deseni ile API'lardan (Retrofit) ve yerel veritabanlarından (Room) gelen veriyi soyutlar.

Kullanılan Teknolojiler:
Dil: Kotlin

UI Toolkit: Jetpack Compose

Mimarisi: MVI (Model-View-Intent)

Bağımlılık Yönetimi: Hilt (Dagger-Hilt)

Asenkron Programlama: Kotlin Coroutines & Flow

Veritabanı: Room Database

Networking: Retrofit

Görsel İşleme: Coil

Navigasyon: Jetpack Compose Navigation

AR: (İlgili AR kütüphanesi, örn. ARCore veya özel bir kütüphane)

📂 Klasör Yapısı
src/main/java/com/example/photoeditor/
│
├── data/                    # Veri Katmanı
│   ├── local/               # Yerel Veri Kaynakları (Room Database, SharedPrefs)
│   │   ├── dao/             # Veritabanı Erişim Objeleri (DAO)
│   │   └── entity/          # Veritabanı Tablo Objeleri
│   ├── remote/              # Uzak Veri Kaynakları (API)
│   │   └── api/             # API Servisleri
│   └── repository/          # Veri Kaynaklarını Soyutlayan Depo Sınıfları
│
├── di/                      # Bağımlılık Enjeksiyonu
│   └── AppModule.kt
│
├── domain/                  # Alan Katmanı (İş Mantığı)
│   ├── model/               # İş Modelleri
│   └── usecase/             # Kullanım Senaryoları (İş Kuralları)
│
├── presentation/            # UI Katmanı (MVI Mimarisi)
│   ├── login/               # Giriş Ekranı
│   ├── register/            # Kayıt Ekranı
│   ├── photoshot/           # Fotoğraf Çekme Ekranı
│   ├── analyze/             # Fotoğraf Düzenleme Ekranı
│   ├── album/               # Albüm Ekranı
│   └── theme/               # Özel Tema Tanımları
│
├── navigation/              # Jetpack Compose Navigasyon
│   └── NavGraph.kt
│
├── utils/                   # Yardımcı Sınıflar ve Fonksiyonlar
│   ├── FilterType.kt
│   └── ImageUtils.kt
│
└── MainActivity.kt
🚀 Kurulum ve Çalıştırma
Bu projeyi yerel ortamınızda çalıştırmak için aşağıdaki adımları takip edin:

Projeyi klonlayın:

Bash

git clone https://github.com/KullaniciAdiniz/photo-editor-app.git
Android Studio'yu açın ve projeyi içe aktarın.

Gradle'ın bağımlılıkları indirmesini bekleyin.

Sanallaştırılmış bir cihazda veya fiziksel bir cihazda uygulamayı çalıştırın.

🤝 Katkıda Bulunma
Bu projenin gelişimine katkıda bulunmak isterseniz, lütfen bir Pull Request (PR) gönderin. Katkılarınızla uygulamayı daha iyi hale getirebiliriz.
