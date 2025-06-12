# BEM HTML Inspector

**Created by Roman Borisov**

Inspection for checking HTML markup for compliance with the BEM methodology for PhpStorm 2025.1+

## Features
- Checks that an element with a class `block__element` is inside a parent with a class `block`.
- Supported files: `.html`, `.blade.php`.
- Highlights the error and shows the message:
  > Element 'block__element' is used outside of block 'block'.

## Example

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

## Extension
The architecture allows you to add new inspection rules via new classes.

## Usage examples

**Blade:**
```html
<div class="menu">
  <div class="menu__item"></div>
</div>
```

**HTML:**
```html
<div class="header">
  <span class="header__logo"></span>
</div>
```

## License
MIT 
