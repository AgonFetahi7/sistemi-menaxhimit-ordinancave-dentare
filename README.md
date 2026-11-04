### AgonFetahi-Sistemi për Menaxhimin e Ordinancave Dentare
  tema:Sistemi për Menaxhimin e Ordinancave Dentare
## Ideja

Ideja është një sistem për menaxhimin e ordinancave dentare, që e bën më të lehtë për stafin dhe dentistët
organizimin e punës së përditshme. Pacientët, dentistët, shërbimet dhe terminet ruhen në një vend të vetëm,
në vend të dokumenteve dhe mjeteve të ndara. Sistemi gjithashtu e parandalon automatikisht rastin kur një
dentist merr dy termine në të njëjtën kohë.

## Kërkesat

Çfarë mund të bëjë sistemi:

- stafi mund të regjistrojë pacientë dhe t'i kërkojë sipas emrit
- administratori mund të shtojë dhe përditësojë dentistët dhe shërbimet (çdo shërbim ka kohëzgjatje)
- stafi mund të rezervojë termin duke zgjedhur pacientin, dentistin, shërbimin dhe orën
- sistemi nuk e lejon terminin nëse dentisti ka tashmë një termin tjetër në atë kohë
- terminet mund të ndryshohen ose anulohen
- terminet mund të filtrohen sipas datës, dentistit dhe statusit
- dentisti mund të regjistrojë shënime për vizitat e përfunduara (në vijim)

Çfarë duhet të ketë sistemi përveç kësaj:

- fushat e detyrueshme kontrollohen dhe gabimet shfaqen me mesazh të kuptueshëm
- fjalëkalimet ruhen vetëm të hash-uara, dhe qasja kufizohet sipas rolit (planifikuar)
- aplikacioni të përdoret edhe në ekran mobil (kur të ketë frontend)
- të dhënat testuese janë të rreme, pa të dhëna reale shëndetësore

## Teknologjitë

- **Frontend:** React (planifikuar për Raportin 3)
- **Backend:** Java 17 dhe Spring Boot, me REST API
- **Databaza:** MySQL
- **Dokumentimi i API-së:** Swagger UI
- **Versionimi:** Git dhe GitHub

Arkitektura është e thjeshtë: frontend-i i dërgon kërkesat backend-it, dhe backend-i i ruan ose i lexon të dhënat nga databaza.

```
React  →  REST API  →  Java / Spring Boot  →  MySQL
```

## Databaza

| Tabela | Për çfarë shërben |
|---|---|
| patient | Pacientët: emri, mbiemri, telefoni, email-i |
| dentist | Dentistët dhe kontaktet e tyre |
| service | Shërbimet dentare dhe sa zgjasin (në minuta) |
| appointment | Terminet: lidh pacientin, dentistin dhe shërbimin, me fillim, fund dhe status |
| visit | Shënimi i vizitës për një termin |

Diagrama e plotë e databazës është te [Raporti Teknik 2](docs/Raporti-Teknik-2.md).

## Si ecën puna

- [x] Raporti Teknik 1: analiza, kërkesat dhe dizajni
- [x] Databaza dhe të dhënat testuese
- [x] Backend dhe API: pacientët, dentistët, shërbimet, terminet dhe kontrolli i orarit
- [ ] Vizitat
- [ ] Frontend me React
- [ ] Autentikimi dhe rolet
- [ ] Testimi dhe deployment

## Raportet

- [Raporti Teknik 1](docs/Raporti-Teknik-1.md) ([Word](docs/Raporti-Teknik-1.docx)): analiza, kërkesat dhe dizajni teknik
- [Raporti Teknik 2](docs/Raporti-Teknik-2.md): databaza, backend-i dhe API

## Si ekzekutohet

1. Instalo Java 17, Maven dhe MySQL.
2. Krijo databazën dhe të dhënat testuese:
   ```bash
   mysql -u root -p < db/schema.sql
   mysql -u root -p < db/seed.sql
   ```
3. Vendos të dhënat e hyrjes në databazë si variabla ambienti (nuk ruhen në repository):
   ```bash
   # Linux / macOS
   export DB_USER=root
   export DB_PASSWORD=fjalekalimi_yt

   # Windows PowerShell
   $env:DB_USER="root"; $env:DB_PASSWORD="fjalekalimi_yt"
   ```
4. Nise aplikacionin:
   ```bash
   mvn spring-boot:run
   ```
5. Hap Swagger UI për t'i provuar endpoint-et: http://localhost:8080/swagger-ui.html

Lista e plotë e endpoint-eve dhe shembujt e gabimeve janë te [Raporti Teknik 2](docs/Raporti-Teknik-2.md).

## Shembull i shpejtë

Rezervimi i një termini (dentisti 1, shërbimi 1, pacienti 1):

```bash
curl -X POST localhost:8080/api/appointments -H "Content-Type: application/json" \
  -d '{"patientId":1,"dentistId":1,"serviceId":1,"startAt":"2026-11-20T12:00:00"}'
```

Nëse dentisti ka tashmë termin në atë kohë, sistemi kthen gabimin `409 SCHEDULE_CONFLICT` dhe nuk e ruan terminin.
