# BEM HTML Inspector

**Created by Roman Borisov**

Inspection for checking HTML markup for compliance with the BEM methodology for PhpStorm 2025.1+

## Features
- Checks that an element with a class `block__element` is inside a parent with a class `block`.
- Checks that a modifier class (`block--modifier` or `block__element--modifier`) is used together with its base class (`block` or `block__element`).
- Supported files: `.html`, `.blade.php`.
- Highlights the error and shows a message with details.


## Installation and Build

### Requirements
- JDK 17
- IntelliJ IDEA Community/Ultimate or PhpStorm 2025.1+
- Gradle (or use the built-in wrapper)

### Build

1. Clone the repository:
   ```bash
   git clone https://github.com/RomanBorisov/bem-validator-plugin.git
   cd bem-html-inspector
   ```
2. Build the plugin:
   ```bash
   ./gradlew buildPlugin
   ```
   The ready-to-install file will appear in `build/distributions/`.

### Install in PhpStorm
1. Open PhpStorm → Settings → Plugins → ⚙️ → Install Plugin from Disk...
2. Select the built `.zip` from `build/distributions/`.
3. Restart the IDE.

## Testing

To run tests:
```bash
./gradlew test
```

## Rules and Examples

### 1. Element must be inside its block
<details>
<summary>Show examples</summary>

**Error:**
```html
<!-- Error: no parent with class 'menu' -->
<div class="menu__item"></div>
```
**Correct:**
```html
<div class="menu">
  <div class="menu__item"></div>
</div>
```
</details>

### 2. Modifier must be used together with block or element
<details>
<summary>Show examples</summary>

**Error:**
```html
<!-- Error: modifier without base class -->
<div class="menu__item--active"></div>
<div class="menu--active"></div>
```
**Correct:**
```html
<div class="menu__item menu__item--active"></div>
<div class="menu menu--active"></div>
```
</details>

### 3. No element or modifier without block
<details>
<summary>Show examples</summary>

**Error:**
```html
<!-- Error: class starts with separator without block name -->
<div class="__item"></div>
<div class="--active"></div>
```
**Correct:**
```html
<div class="menu__item"></div>
<div class="menu--active"></div>
```
</details>

## License
MIT 
