# Bank Cards Management System

A comprehensive Java-based console application for managing bank cards, clients, and card operations with integrated fraud detection capabilities.

## Contexte du projet

La gestion des cartes bancaires et détection de fraude est devenue une priorité pour les banques.

Chaque client possède une ou plusieurs cartes (débit, crédit, prépayée). Ces cartes génèrent des transactions diverses (paiement, retrait, achat en ligne).

Les banques doivent :

• Gérer le cycle de vie d'une carte (création, activation, suspension)

• Suivre en temps réel les opérations liées aux cartes
• Détecter automatiquement des comportements suspects (ex. achats dans deux pays différents à quelques minutes d'intervalle)
• Alerter les responsables ou bloquer la carte en cas de fraude potentielle

## Features

### Client Management
- Create new clients with validation (name, email, phone)
- Display all clients in a formatted table
- Validate client information (10-digit phone numbers, proper email format)
- Search clients by email (ClientService)

### Card Management
- Create different types of cards for existing clients:
  - **Credit Cards** - with monthly limit and interest rate
  - **Debit Cards** - with daily spending limit
  - **Prepaid Cards** - with initial balance
- Automatic card number generation (10 digits, unique)
- Card lifecycle management (creation, activation, suspension)
- Card status management (ACTIVE, SUSPENDED, BLOCKED)
- Spending limit verification before authorizing operations

### Operations & History
- Track card operations with detailed information:
  - **PURCHASE** - In-store purchases
  - **WITHDRAWAL** - ATM withdrawals
  - **ONLINE_PAYMENT** - Online transactions
- View complete card operation history
- Real-time operation tracking

### Fraud Detection & Alerts
- Automatic anomaly detection:
  - High amount transactions
  - Operations in different locations within short time intervals
  - Spending limit violations
- Generate fraud alerts with severity levels:
  - **INFO** - Informational alerts
  - **WARNING** - Suspicious activities
  - **CRITICAL** - High-risk fraud detection
- Automatic card blocking for critical fraud cases

### Reporting & Analytics
- **Top 5 most used cards** statistics
- **Monthly operation statistics** by type
- **Blocked/suspicious cards** listing
- Import/Export capabilities for Excel files

## Technology Stack

- **Language**: Java (JDK 8+)
- **Database**: SQL Database connectivity with JDBC
- **Architecture**: Layered Architecture (Presentation → Service → DAO → Entity)
- **Design Patterns**: 
  - DAO Pattern for data access
  - Singleton-like pattern for DAO management
  - Record pattern for immutable entities
  - Sealed classes for card type hierarchy

## Application Structure

### Couche présentation (UI/Menu)
Interface textuelle avec navigation par menu pour toutes les opérations bancaires.

### Couche métier (Services)
Logique applicative pour gérer les cartes, les transactions associées, et les alertes de fraude.

### Couche Entity
Objets persistants avec design patterns modernes :
- **record** pour OperationCarte (immutable)
- **sealed** pour Carte (Débit, Crédit, Prépayée)

### Couche DAO
Gestion CRUD complète pour cartes, opérations, clients et alertes de fraude.

### Couche utilitaire
- Vérification des règles de fraude
- Gestion des dates et lieux
- Génération de numéros de carte uniques
- Validation des données d'entrée

## Project Structure

```
src/
├── Main.java                 # Application entry point
├── DAO/                      # Data Access Layer
│   ├── ClientDAO.java        # Client database operations
│   ├── CardDAO.java          # Card database operations
│   ├── OperationDAO.java     # Operation database operations
│   ├── FraudAlertDAO.java    # Fraud alert database operations
│   └── ConnectionDAO.java    # Database connection management
├── entity/                   # Entity classes
│   ├── Client.java           # Client record (id, nom, email, téléphone)
│   ├── Card.java             # Abstract sealed card class
│   ├── CreditCard.java       # Credit card (plafond mensuel, taux d'intérêt)
│   ├── DebitCard.java        # Debit card (plafond journalier)
│   ├── PrepaidCard.java      # Prepaid card (solde disponible)
│   ├── CardOperation.java    # Operation record (immutable)
│   ├── FraudAlert.java       # Fraud alert record
│   └── enums/                # Enumeration classes
│       ├── Status.java       # Card status (ACTIVE, SUSPENDED, BLOCKED)
│       ├── Level.java        # Alert level (INFO, WARNING, CRITICAL)
│       └── Type.java         # Operation type (PURCHASE, WITHDRAWAL, ONLINE_PAYMENT)
├── service/                  # Business Logic Layer
│   ├── ClientService.java    # Client management services
│   ├── CardService.java      # Card lifecycle management
│   ├── OperationService.java # Transaction processing
│   ├── FraudService.java     # Fraud detection algorithms
│   ├── ReportService.java    # Analytics and reporting
│   └── ExportService.java    # Import/Export Excel functionality
├── UI/                       # User Interface Layer
│   └── Menu.java             # Console menu system
└── util/                     # Utility classes
    ├── InputHelpers.java     # Input validation helpers
    ├── Validators.java       # Data validation utilities
    ├── FraudRules.java       # Fraud detection rules
    ├── DateFormatter.java    # Date/time utilities
    └── CardNumberGenerator.java # Unique card number generation
```

## Database Schema

### Relations
- **1..n** entre Client et Carte
- **1..n** entre Carte et OperationCarte  
- **1..n** entre Carte et AlerteFraude

### Tables
```sql
-- Client table
client (id, nom, email, téléphone)

-- Card table  
carte (id, numero, dateExpiration, statut, typeCarte, idClient, 
       plafondJournalier, plafondMensuel, tauxInteret, soldeDisponible)

-- Operation table
operationCarte (id, date, montant, type, lieu, idCarte)

-- Fraud Alert table
alerteFraude (id, description, niveau, idCarte, dateCreation)
```

## Menu Options

1. **Create a Client** - Add new clients to the system
2. **Create a Card** - Issue cards (debit, credit, prepaid) for existing clients
3. **Make an Operation** - Process transactions (purchase, withdrawal, online payment)
4. **View Card History** - Display complete transaction history
5. **Fraud Analysis** - Run fraud detection algorithms and generate alerts
6. **Suspend/Block Card** - Manage card status and security
7. **Reports & Analytics** - Generate usage statistics and reports
8. **Import/Export** - Handle Excel file operations
9. **Exit** - Close the application

## How to Run

1. Ensure you have Java installed (JDK 8 or higher)
2. Set up your database connection in `ConnectionDAO.java`
3. Create the required database tables (see Database Schema)
4. Compile the project: `javac -cp src src/Main.java`
5. Run the application: `java -cp src Main`
