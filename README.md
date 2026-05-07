> ⚠️ Este README es generado automáticamente mediante GitHub Actions.  
> Cualquier cambio manual será sobrescrito.

# BasePage Automation
Clase reutilizable que contiene métodos para interactuar con elementos DOM de una página web.
![BasePage Automation](assets/preview_base_page_automation.png)

## Web utilizada
Para los test estoy usando la página web: https://demoqa.com/ como sandbox.

---
<!-- TREE:START -->
## 📁 Estructura del proyecto

```
base-page-automation/
├── .gitignore
├── build.gradle
├── generate_readme.py
├── gradle.properties
├── gradlew
├── gradlew.bat
├── LICENSE
├── README.md
├── settings.gradle
└── src/
    ├── main
    │   └── java
    │       ├── data
    │       │   └── EmployeeFactory.java
    │       ├── driverManager
    │       │   └── DriverManager.java
    │       ├── model
    │       │   ├── CheckboxNode.java
    │       │   ├── Employee.java
    │       │   ├── ResolverCheckboxLabel.java
    │       │   └── UserData.java
    │       ├── pages
    │       │   ├── alertFrame
    │       │   │   ├── AlertsPage.java
    │       │   │   ├── AlertsWindowsPage.java
    │       │   │   ├── BrowserWindowsPage.java
    │       │   │   ├── FramesPage.java
    │       │   │   ├── ModalDialogPage.java
    │       │   │   └── NestedFramesPage.java
    │       │   ├── bookStoreApplication
    │       │   │   ├── BooksPage.java
    │       │   │   ├── BookStoreApiPage.java
    │       │   │   ├── BookStorePage.java
    │       │   │   ├── LoginPage.java
    │       │   │   └── ProfilePage.java
    │       │   ├── elements
    │       │   │   ├── BrokenLinksPage.java
    │       │   │   ├── ButtonsPage.java
    │       │   │   ├── CheckboxPage.java
    │       │   │   ├── DynamicPropertiesPage.java
    │       │   │   ├── ElementsPage.java
    │       │   │   ├── LinksPage.java
    │       │   │   ├── RadioButtonPage.java
    │       │   │   ├── TextBoxPage.java
    │       │   │   ├── UploadDownloadPage.java
    │       │   │   └── WebTablesPage.java
    │       │   ├── forms
    │       │   │   ├── FormsPage.java
    │       │   │   └── PracticeForm.java
    │       │   ├── interactions
    │       │   │   ├── DragabblePage.java
    │       │   │   ├── DroppablePage.java
    │       │   │   ├── InteractionPage.java
    │       │   │   ├── ResizablePage.java
    │       │   │   ├── SelectablePage.java
    │       │   │   └── SortablePage.java
    │       │   ├── widgets
    │       │   │   ├── AccordianPage.java
    │       │   │   ├── AutoCompletePage.java
    │       │   │   ├── DatePickerPage.java
    │       │   │   ├── MenuPage.java
    │       │   │   ├── ProgressBarPage.java
    │       │   │   ├── SelectMenuPage.java
    │       │   │   ├── SliderPage.java
    │       │   │   ├── TabsPage.java
    │       │   │   ├── TooltipsPage.java
    │       │   │   └── WidgetsPage.java
    │       │   ├── BasePage.java
    │       │   └── HomePage.java
    │       └── valueObject
    │           ├── CheckboxLabel.java
    │           └── CheckboxState.java
    └── test
        ├── java
        │   ├── hooks
        │   │   └── Hooks.java
        │   ├── runner
        │   │   └── TestRunner.java
        │   └── steps
        │       ├── ButtonsSteps.java
        │       ├── CheckBoxSteps.java
        │       ├── LinksSteps.java
        │       ├── NavigationSteps.java
        │       ├── RadioButtonSteps.java
        │       ├── TextboxSteps.java
        │       └── WebTableSteps.java
        └── resources
            └── features
                ├── business
                │   ├── buttons.feature
                │   ├── checkbox.feature
                │   ├── links.feature
                │   ├── radiobutton.feature
                │   ├── textbox.feature
                │   └── webtables.feature
                └── internal
                    └── checkboxInternal.feature
```

<!-- TREE:END -->

<!-- METHODS:START -->
## 📋 Métodos disponibles (22)

| Clase | Visibilidad | Método | Descripción | Parámetros | Retorna | # Usos |
|-------|-------------|--------|-------------|------------|---------|--------|
| <sub>`BasePage.java`</sub> | <sub>`private`</sub> | <sub>`getWebElementClickable()`</sub> | <sub>Espera a que un elemento esté disponible para hacer click en el DOM y lo retorna</sub> | <sub>`String locator`: XPath del elemento a buscar</sub> | <sub>WebElement encontrado en el DOM</sub> | <sub>**4**</sub> |
| <sub>`BasePage.java`</sub> | <sub>`private`</sub> | <sub>`getOptionsSelect()`</sub> | <sub>Genera una lista de WebElements en base al Select del DOM y lo retorna</sub> | <sub>`String locator`: XPath del Select a extraer las opciones</sub> | <sub>List<WebElement> armado con las opciones</sub> | <sub>**1**</sub> |
| <sub>`BasePage.java`</sub> | <sub>`public`</sub> | <sub>`isLocatorPresent()`</sub> | <sub>Nos dice si un locator está presente</sub> | <sub>`String locator`: XPath del elemento web que queremos buscar</sub> | <sub>boolean true si está, caso contrario false</sub> | <sub>**4**</sub> |
| <sub>`BasePage.java`</sub> | <sub>`public`</sub> | <sub>`getWebElementPresent()`</sub> | <sub>Espera a que un elemento esté presente en el DOM y lo retorna</sub> | <sub>`String locator`: XPath del elemento a buscar</sub> | <sub>WebElement encontrado en el DOM</sub> | <sub>**3**</sub> |
| <sub>`BasePage.java`</sub> | <sub>`public`</sub> | <sub>`navigateTo()`</sub> | <sub>Ingresa a URL en el navegador</sub> | <sub>`String url`: Dirección web a la cual queremos dirigirnos</sub> | <sub>—</sub> | <sub>**1**</sub> |
| <sub>`BasePage.java`</sub> | <sub>`public`</sub> | <sub>`click()`</sub> | <sub>Hace click en el locator indicado</sub> | <sub>`String locator`: XPath del locator que queremos hacerle click</sub> | <sub>—</sub> | <sub>**49**</sub> |
| <sub>`BasePage.java`</sub> | <sub>`public`</sub> | <sub>`getTextWebElement()`</sub> | <sub>Obtiene el texto de un elemento web del DOM</sub> | <sub>`String locator`: XPath del locator que queremos su texto</sub> | <sub>String del texto en base al locator</sub> | <sub>**9**</sub> |
| <sub>`BasePage.java`</sub> | <sub>`public`</sub> | <sub>`setValueOnWebElement()`</sub> | <sub>Escribir texto en el elemento web del DOM</sub> | <sub>`String locator`: XPath del locator que queremos escribir<br>`String value`: Texto que queremos escribir</sub> | <sub>—</sub> | <sub>**3**</sub> |
| <sub>`BasePage.java`</sub> | <sub>`public`</sub> | <sub>`getListOptionsSelect()`</sub> | <sub>Genera una lista de Strings en base al Select del DOM y lo retorna</sub> | <sub>`String locator`: XPath del Select a extraer las opciones</sub> | <sub>List<String> armado con las opciones</sub> | <sub>0</sub> |
| <sub>`BasePage.java`</sub> | <sub>`public`</sub> | <sub>`selectOption()`</sub> | <sub>Selecciona una opción dentro de un Select de un elemento Web</sub> | <sub>`String locator`: XPath del Select para elegir la opción<br>`String option`: String que indica la opción que vamos a elegir en el Select</sub> | <sub>—</sub> | <sub>**2**</sub> |
| <sub>`BasePage.java`</sub> | <sub>`public`</sub> | <sub>`getValidationMessageWebElement()`</sub> | <sub>Extrae el tooltip de mensaje de validación que nos devuelve un elemento web</sub> | <sub>`String locator`: XPath del elemento web para extraer el tooltip</sub> | <sub>String texto del tooltip</sub> | <sub>**1**</sub> |
| <sub>`BasePage.java`</sub> | <sub>`public`</sub> | <sub>`clickAll()`</sub> | <sub>Hace click en todos los locators que encuentre con ese xpath</sub> | <sub>`String locator`: XPath de los locators que queremos hacerle click</sub> | <sub>—</sub> | <sub>**2**</sub> |
| <sub>`BasePage.java`</sub> | <sub>`public`</sub> | <sub>`openAllCloseSwitcher()`</sub> | <sub>Abre todos los switcher + cerrados visualizados en la página</sub> | <sub>`String locator`: XPath del elemento web switcher</sub> | <sub>—</sub> | <sub>0</sub> |
| <sub>`BasePage.java`</sub> | <sub>`public`</sub> | <sub>`getAllWebElements()`</sub> | <sub>Devuelve todos los elementos web que coincidan con el locator, no espera</sub> | <sub>`String locator`: xPath del elemento web a buscar</sub> | <sub>List<WebElement> listado de elementos encontrados a devolver</sub> | <sub>**3**</sub> |
| <sub>`BasePage.java`</sub> | <sub>`public`</sub> | <sub>`getWebElementInside()`</sub> | <sub>Devuelve el elemento web que coincidan con el locator pero dentro de otro elemento web</sub> | <sub>`WebElement webElement`: elemento web padre<br>`String locatorIn`: xPath del elemento web a buscar dentro del webElement</sub> | <sub>WebElement elemento web buscado</sub> | <sub>**2**</sub> |
| <sub>`BasePage.java`</sub> | <sub>`public`</sub> | <sub>`getWebElement()`</sub> | <sub>Devuelve el elemento web que coincidan con el locator</sub> | <sub>`String locator`: xPath del elemento web a buscar</sub> | <sub>WebElement elemento web buscado, si no existe devuelvo null</sub> | <sub>**1**</sub> |
| <sub>`BasePage.java`</sub> | <sub>`public`</sub> | <sub>`isLocatorEnabled()`</sub> | <sub>Nos dice si un locator está habilitado</sub> | <sub>`String locator`: XPath del elemento web que queremos buscar</sub> | <sub>boolean true si está, caso contrario false</sub> | <sub>**3**</sub> |
| <sub>`BasePage.java`</sub> | <sub>`public`</sub> | <sub>`safeClick()`</sub> | <sub>Hace click en el locator indicado pero de forma segura</sub> | <sub>`String locator`: XPath del locator que queremos hacerle click</sub> | <sub>—</sub> | <sub>**2**</sub> |
| <sub>`BasePage.java`</sub> | <sub>`public`</sub> | <sub>`getWebElementVisible()`</sub> | <sub>Espera a que un elemento esté visible en el DOM y lo retorna</sub> | <sub>`String locator`: XPath del elemento a buscar</sub> | <sub>WebElement encontrado en el DOM</sub> | <sub>**2**</sub> |
| <sub>`BasePage.java`</sub> | <sub>`public`</sub> | <sub>`waitUntilPageChange()`</sub> | <sub>Valida si se ha realizado paginación comparando los textos del paginado</sub> | <sub>`String locatorPage`: XPath texto del paginado<br>`String previousPage`: texto de la página previa</sub> | <sub>—</sub> | <sub>**1**</sub> |
| <sub>`BasePage.java`</sub> | <sub>`public`</sub> | <sub>`rightClick()`</sub> | <sub>Hace click derecho en el locator indicado</sub> | <sub>`String locator`: XPath del locator que queremos hacerle click derecho</sub> | <sub>—</sub> | <sub>**2**</sub> |
| <sub>`BasePage.java`</sub> | <sub>`public`</sub> | <sub>`doubleClick()`</sub> | <sub>Hace doble click en el locator indicado</sub> | <sub>`String locator`: XPath del locator que queremos hacerle doble click</sub> | <sub>—</sub> | <sub>**2**</sub> |

<!-- METHODS:END -->

<!-- CHART:START -->
## 📊 Mapa de uso de métodos

> Cada burbuja representa un método de `BasePage`.
> El **tamaño** y la **intensidad de color** indican cuántas veces ha sido utilizado.

<img src="docs/bubble_chart.png" width="100%" alt="BasePage Methods Usage Chart"/>

<!-- CHART:END -->

