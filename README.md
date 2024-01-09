# Fahrkarten-Opener

Dies ist eine einfache Android-App, die es dem Benutzer ermöglicht, ein Bild aus seiner Galerie auszuwählen und es in der App anzuzeigen. Das ausgewählte Bild wird im internen Speicher der App gespeichert und beim nächsten Start der App geladen.

## Funktionen

- Auswahl eines Bildes aus der Galerie
- Anzeige des ausgewählten Bildes in der App
- Speichern des ausgewählten Bildes im internen Speicher der App
- Laden des gespeicherten Bildes beim Start der App

## Verwendung

1. Starten Sie die App. Wenn ein Bild im internen Speicher gespeichert ist, wird es angezeigt.
2. Klicken Sie auf den Floating Action Button, um ein Bild aus Ihrer Galerie auszuwählen.
3. Das ausgewählte Bild wird in der App angezeigt und im internen Speicher gespeichert.

## Technische Details

Die App verwendet die Android-Bibliotheken `android.graphics.Bitmap` und `android.graphics.BitmapFactory` zum Arbeiten mit Bildern und `android.content.Intent` zum Abrufen von Bildern aus der Galerie. Sie verwendet auch die `androidx.appcompat.app.AppCompatActivity`-Klasse für die Hauptaktivität der App.

Die App speichert das ausgewählte Bild im internen Speicher mit dem Dateinamen "selectedImage.jpg". Beim Start der App wird versucht, diese Datei zu laden und anzuzeigen.

## Fehlerbehandlung

Die App fängt `FileNotFoundException` und `IOException` ab, die beim Arbeiten mit Bildern und Dateien auftreten können. Im Falle eines solchen Fehlers wird der Stacktrace des Fehlers ausgegeben.

![image](https://github.com/Moon-Mind/Fahrkarten_openner/assets/52799853/cfd3ee29-81be-4488-8209-f7ea8762c1eb)

okplpl
