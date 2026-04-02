# BasePage Automation
Clase reutilizable que contiene métodos para interactuar con elementos DOM de una página web.
![BasePage Automation](assets/preview_base_page_automation.png)
## Web utilizada

Para los test estoy usando la página web: https://demoqa.com/ como sandbox.

---

## Estructura del proyecto

```
src/
└── test/
    └── java/
        ├── driverManager/
        │   └── DriverManager.java
        ├── hooks/
        │   └── Hooks.java
        ├── pages/
        │   └── BasePage.java
        └── runner/
            └── TestRunner.java
```

---

## Métodos disponibles en BasePage

### Navegación

#### `navigateTo(String url)`
Navega a la URL indicada.
```java
basePage.navigateTo("https://demoqa.com/text-box");
```

---

### Interacción con elementos

#### `click(String locator)`
Hace click en un elemento del DOM. Espera a que el elemento sea clickeable antes de interactuar.
```java
basePage.click("//button[@id='submit']");
```

#### `getTextWebElement(String locator)`
Retorna el texto visible de un elemento del DOM. Espera a que el elemento esté presente en el DOM.
```java
String mensaje = basePage.getTextWebElement("//p[@id='name']");
```

---

### Interacción con Select

#### `getListOptionsSelect(String locator)`
Retorna una lista con el texto de todas las opciones de un elemento `<select>`. Espera a que todas las opciones estén cargadas antes de retornarlas.
```java
List<String> opciones = basePage.getListOptionsSelect("//select[@id='oldSelectMenu']");
```

#### `selectOption(String locator, String option)`
Selecciona una opción de un elemento `<select>` por su texto visible.
```java
basePage.selectOption("//select[@id='oldSelectMenu']", "Purple");
```

---

## Métodos privados (uso interno)

| Método | Condición de espera | Usado por |
|---|---|---|
| `getWebElementPresent` | Elemento presente en el DOM | `getTextWebElement`, `selectOption` |
| `getWebElementClickable` | Elemento visible y clickeable | `click` |
| `getOptionsSelect` | Select visible y con opciones cargadas | `getListOptionsSelect`, `selectOption` |

---

## Notas

- El tiempo de espera por defecto es **20 segundos**.
- Todos los locators se reciben como **XPath**.
- La instancia del `WebDriver` se obtiene desde `DriverManager`.