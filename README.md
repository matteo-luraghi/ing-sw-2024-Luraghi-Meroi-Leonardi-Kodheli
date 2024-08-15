# Progetto di Ingegneria del Software A.A. 2023/2024

🕹️ Realizzazione in formato software del gioco da tavolo ***Codex Naturalis***, regolamento reperibile al 
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

🎯 Voto finale: 29/30L

## Istruzioni per eseguire i jar

📂 I file necessitano della versione di Java 21, scaricabile al [link](https://www.oracle.com/it/java/technologies/downloads/)

⌨️  Aprire il terminale nella cartella Deliverables/final/JAR (o nella cartella dove risiedono i .jar) e digitare i seguenti comandi:
- Per eseguire il server `java -jar ./server.jar`
- Per eseguire il client con l'interfaccia grafica `java -jar ./client.jar` (su windows è possibile fare doppio click sul .jar)
- Per eseguire il client con l'interfaccia testuale `java -jar ./client.jar -cli`

## Disclaimer
NOTA: Codex Naturalis è un gioco da tavolo sviluppato ed edito da Cranio Creations Srl. I contenuti grafici di questo progetto riconducibili al prodotto editoriale da tavolo sono utilizzati previa approvazione di Cranio Creations Srl a solo scopo didattico. È vietata la distribuzione, la copia o la riproduzione dei contenuti e immagini in qualsiasi forma al di fuori del progetto, così come la redistribuzione e la pubblicazione dei contenuti e immagini a fini diversi da quello sopracitato. È inoltre vietato l'utilizzo commerciale di suddetti contenuti.
