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
src/test/java/
├── driverManager
│   └── DriverManager.java
├── hooks
│   └── Hooks.java
├── pages
│   ├── BasePage.java
│   └── DemoQA.java
├── runner
│   └── TestRunner.java
└── steps
    └── InteractDemoQA.java
src/test/resources/
└── features
    └── interactDemoQA.feature
```

<!-- TREE:END -->

<!-- MERMAID:START -->
## 🔗 Relación Scenario → Métodos

```mermaid
flowchart TD
    Interact_with_the_sandbox_page_https_demoqa_com_["📄 Interact with the sandbox page https://demoqa.com/"]
    Interact_with_the_sandbox_page_https_demoqa_com__The_user_can_fill_section_Text_Box_and_click_the_button_Submit["🎬 The user can fill section Text Box and c"]
    Interact_with_the_sandbox_page_https_demoqa_com_ --> Interact_with_the_sandbox_page_https_demoqa_com__The_user_can_fill_section_Text_Box_and_click_the_button_Submit
    Interact_with_the_sandbox_page_https_demoqa_com__The_user_can_fill_section_Text_Box_and_click_the_button_Submit_the_user_go_to_the_page_DemoQa["⚡ Given the user go to the page DemoQa"]
    Interact_with_the_sandbox_page_https_demoqa_com__The_user_can_fill_section_Text_Box_and_click_the_button_Submit --> Interact_with_the_sandbox_page_https_demoqa_com__The_user_can_fill_section_Text_Box_and_click_the_button_Submit_the_user_go_to_the_page_DemoQa
    Interact_with_the_sandbox_page_https_demoqa_com__The_user_can_fill_section_Text_Box_and_click_the_button_Submit_click_on_the_elements_section["⚡ When click on the elements section"]
    Interact_with_the_sandbox_page_https_demoqa_com__The_user_can_fill_section_Text_Box_and_click_the_button_Submit --> Interact_with_the_sandbox_page_https_demoqa_com__The_user_can_fill_section_Text_Box_and_click_the_button_Submit_click_on_the_elements_section
    page_clickOnElementsSection["🔧 clickOnElementsSection()"]
    Interact_with_the_sandbox_page_https_demoqa_com__The_user_can_fill_section_Text_Box_and_click_the_button_Submit_click_on_the_elements_section --> page_clickOnElementsSection
    bp_click["🏗️ BasePage::click()"]
    page_clickOnElementsSection --> bp_click
    Interact_with_the_sandbox_page_https_demoqa_com__The_user_can_fill_section_Text_Box_and_click_the_button_Submit_click_on_the_text_box_accordio["⚡ And click on the text box accordion option"]
    Interact_with_the_sandbox_page_https_demoqa_com__The_user_can_fill_section_Text_Box_and_click_the_button_Submit --> Interact_with_the_sandbox_page_https_demoqa_com__The_user_can_fill_section_Text_Box_and_click_the_button_Submit_click_on_the_text_box_accordio
    page_clickOnAccordionTextBoxOption["🔧 clickOnAccordionTextBoxOption()"]
    Interact_with_the_sandbox_page_https_demoqa_com__The_user_can_fill_section_Text_Box_and_click_the_button_Submit_click_on_the_text_box_accordio --> page_clickOnAccordionTextBoxOption
    bp_click["🏗️ BasePage::click()"]
    page_clickOnAccordionTextBoxOption --> bp_click
    Interact_with_the_sandbox_page_https_demoqa_com__The_user_can_fill_section_Text_Box_and_click_the_button_Submit_fill_textboxes_on_the_page["⚡ And fill textboxes on the page"]
    Interact_with_the_sandbox_page_https_demoqa_com__The_user_can_fill_section_Text_Box_and_click_the_button_Submit --> Interact_with_the_sandbox_page_https_demoqa_com__The_user_can_fill_section_Text_Box_and_click_the_button_Submit_fill_textboxes_on_the_page
    page_fillTextboxes["🔧 fillTextboxes()"]
    Interact_with_the_sandbox_page_https_demoqa_com__The_user_can_fill_section_Text_Box_and_click_the_button_Submit_fill_textboxes_on_the_page --> page_fillTextboxes
    bp_setValueOnWebElement["🏗️ BasePage::setValueOnWebElement()"]
    page_fillTextboxes --> bp_setValueOnWebElement
    Interact_with_the_sandbox_page_https_demoqa_com__The_user_can_fill_section_Text_Box_and_click_the_button_Submit_click_the_button_Submit["⚡ And click the button Submit"]
    Interact_with_the_sandbox_page_https_demoqa_com__The_user_can_fill_section_Text_Box_and_click_the_button_Submit --> Interact_with_the_sandbox_page_https_demoqa_com__The_user_can_fill_section_Text_Box_and_click_the_button_Submit_click_the_button_Submit
    Interact_with_the_sandbox_page_https_demoqa_com__The_user_can_fill_section_Text_Box_and_click_the_button_Submit_can_confirm_his_information_in["⚡ Then can confirm his information in the output sec"]
    Interact_with_the_sandbox_page_https_demoqa_com__The_user_can_fill_section_Text_Box_and_click_the_button_Submit --> Interact_with_the_sandbox_page_https_demoqa_com__The_user_can_fill_section_Text_Box_and_click_the_button_Submit_can_confirm_his_information_in
```

<!-- MERMAID:END -->

<!-- METHODS:START -->
## 📋 Métodos disponibles (9)

| Clase | Visibilidad | Método | Descripción | Parámetros | Retorna | # Usos |
|-------|-------------|--------|-------------|------------|---------|--------|
| `BasePage.java` | `private` | `getWebElementPresent()` | Espera a que un elemento esté presente en el DOM y lo retorna | `String locator`: XPath del elemento a buscar | WebElement encontrado en el DOM | 0 |
| `BasePage.java` | `private` | `getWebElementClickable()` | Espera a que un elemento esté disponible para hacer click en el DOM y lo retorna | `String locator`: XPath del elemento a buscar | WebElement encontrado en el DOM | 0 |
| `BasePage.java` | `private` | `getOptionsSelect()` | Genera una lista de WebElements en base al Select del DOM y lo retorna | `String locator`: XPath del Select a extraer las opciones | List<WebElement> armado con las opciones | 0 |
| `BasePage.java` | `public` | `navigateTo()` | Ingresa a URL en el navegador | `String url`: Dirección web a la cual queremos dirigirnos | — | **1** |
| `BasePage.java` | `public` | `click()` | Hace click en el locator indicado | `String locator`: XPath del locator que queremos hacerle click | — | **2** |
| `BasePage.java` | `public` | `getTextWebElement()` | Obtiene el texto de un elemento web del DOM | `String locator`: XPath del locator que queremos su texto | String del texto en base al locator | 0 |
| `BasePage.java` | `public` | `setValueOnWebElement()` | Escribir texto en el elemento web del DOM | `String locator`: XPath del locator que queremos escribir<br>`String value`: Texto que queremos escribir | — | **1** |
| `BasePage.java` | `public` | `getListOptionsSelect()` | Genera una lista de Strings en base al Select del DOM y lo retorna | `String locator`: XPath del Select a extraer las opciones | List<String> armado con las opciones | 0 |
| `BasePage.java` | `public` | `selectOption()` | Selecciona una opción dentro de un Select de un elemento Web | `String locator`: XPath del Select para elegir la opción<br>`String option`: String que indica la opción que vamos a elegir en el Select | — | 0 |

<!-- METHODS:END -->



