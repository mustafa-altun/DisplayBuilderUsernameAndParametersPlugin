# Build User Display Plugin

[Türkçe (Turkish)](#türkçe) | [English](#english)

---

<a id="türkçe"></a>
## Türkçe

Build User Display, Jenkins üzerindeki işleri (job) başlatan kullanıcının bilgisini otomatik olarak işin adına ekleyen basit bir eklentidir.

Varsayılan olarak Jenkins, iş isimlerini sadece yapı numarası ile gösterir (örneğin `#145`). Özellikle Stage View gibi ekranlarda bir işi kimin tetiklediğini ilk bakışta görmek zor olabilir. Bu eklenti devreye girerek yapı adını otomatik olarak `#145 [kullanici.adi]` formatına dönüştürür. Eğer iş manuel olarak değil de otomatik olarak tetiklendiyse, adlandırma `#145 [Auto]` şeklinde gerçekleşir.

Ayrıca eğer tetiklenen iş (job) parametrik ise, parametrelerin adı ve değeri de metin olarak isme dahil edilir. Örnek bir tam görüntülenme adı şu şekildedir: `#145 [kullanici.adi] [Params: Ortam:test11 - Modul:bs]`.

Bu sayede Jenkins'in tüm arayüzlerinde (Stage View, Blue Ocean vb.) ekstra bir konfigürasyona gerek kalmadan kullanıcının kim olduğunu ve hangi parametrelerle işi başlattığını görebilirsiniz.

### Özellikler
* Kurulum sonrası ekstra konfigürasyon gerektirmez.
* İş başladığı anda yapı adını otomatik olarak günceller.
* Zamanlayıcı (timer) veya SCM gibi otomatik tetiklemeleri ayırt edebilir.
* Eğer mevcutsa, iş parametrelerini (hassas veriler maskelenerek) güvenli bir şekilde isme ekler.

### Kurulum
Eklentiyi derledikten sonra oluşan `.hpi` dosyasını Jenkins yönetim panelindeki eklentiler sayfasından yükleyebilirsiniz.

---

<a id="english"></a>
## English

Build User Display is a straightforward Jenkins plugin that automatically appends the name of the user who triggered a job to the build's display name.

By default, Jenkins only displays the build number as the job name (e.g., `#145`). It can be difficult to see who triggered a job at first glance, especially in views like the Pipeline Stage View. This plugin automatically transforms the build name to `#145 [username]`. If the job was triggered automatically rather than manually, the naming format becomes `#145 [Auto]`.

Furthermore, if the triggered job is parameterized, the parameter names and values are also appended to the name as plain text. An example of a full display name looks like this: `#145 [username] [Params: Env:test11 - Module:bs]`.

As a result, you can instantly see who initiated the build and with what parameters across all Jenkins interfaces (Stage View, Blue Ocean, etc.) without needing any extra configuration.

### Features
* No additional configuration is required after installation.
* Automatically updates the build name right when the job starts.
* Can distinguish automatic triggers such as timers or SCM changes.
* Safely appends job parameters (masking sensitive data) to the name as text, if available.

### Installation
After compiling the plugin, you can upload the generated `.hpi` file through the plugins page in the Jenkins management dashboard.
