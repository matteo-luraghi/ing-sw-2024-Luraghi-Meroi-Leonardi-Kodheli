# Progetto di Ingegneria del Software A.A. 2023/2024

🕹️ Realizzazione in formato sowftware del gioco da tavolo ***Codex Naturalis***, regolamento reperibile al 
[sito ufficiale](https://www.craniocreations.it/storage/media/product_downloads/126/1516/CODEX_ITA_Rules_compressed.pdf)

## Componenti del Gruppo

- 🎓 **Matteo Leonardo Luraghi**
- 🎓 **Lorenzo Meroi**
- 🎓 **Gabriel Leonardi**
- 🎓 **Francesk Kodheli**

## Funzionalità Implementate

| Funzionalità                      | Implementata |
|-----------------------------------|--------------|
| Regole complete                   | ✅           |
| TUI                               | ✅           |
| GUI                               | ✅           |
| Connessione con RMI               | ✅           |
| Connessione con Socket            | ✅           |
| Partite Multiple                  | ✅           |
| Chat                              | ✅           |
| Persistenza                       | ❌           |
| Resilienza alle disconnessioni    | ❌           |

## Istruzioni per eseguire i jar

📂 I file necessitano della versione di Java 21, scaricabile al [link](https://www.oracle.com/it/java/technologies/downloads/)

⌨️  Aprire il terminale nella cartella Deliverables/final/JAR (o nella cartella dove risiedono i .jar) e digitare i seguenti comandi:
- Per eseguire il server `java -jar ./server.jar`
- Per eseguire il client con l'interfaccia grafica `java -jar ./client.jar` (su windows è possibile fare doppio click sul .jar)
- Per eseguire il client con l'interfaccia testuale `java -jar ./client.jar -cli`
