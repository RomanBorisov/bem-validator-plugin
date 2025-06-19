# Plugin Signing Guide

## Signing Process Overview

According to the [official JetBrains documentation](https://plugins.jetbrains.com/docs/intellij/plugin-signing.html), plugin signing is a security mechanism introduced in the 2021.2 release cycle to increase security on the JetBrains Marketplace.

### How Signing Works:
1. **Double Signing**: Plugin is signed twice - first by the author, then by JetBrains Marketplace
2. **Integrity Check**: Ensures that plugins are not modified during the publishing and delivery pipeline
3. **Security Warnings**: If the plugin is not signed, IDE will show a warning dialog during installation

## Step-by-Step Instructions

### Step 1: Generate Private Key and Certificate

Execute the following commands in terminal:

```bash
# 1. Generate encrypted RSA private key (4096 bits)
openssl genpkey -aes-256-cbc -algorithm RSA -out private_encrypted.pem -pkeyopt rsa_keygen_bits:4096
```

Enter a strong password and remember it!

```bash
# 2. Convert to standard RSA format
openssl rsa -in private_encrypted.pem -out private.pem
```

Enter the password you created in the previous step.

```bash
# 3. Generate self-signed certificate
openssl req -key private.pem -new -x509 -days 365 -out chain.crt
```

Fill in the certificate information:
- **Country Name**: US (or your country code)
- **State**: Your state/region
- **City**: Your city  
- **Organization**: Your name or organization
- **Organizational Unit**: can be left empty
- **Common Name**: your name
- **Email**: your email

### Step 2: Setup Environment Variables

1. Copy `env.example` to `.env`:
```bash
cp env.example .env
```

2. Edit `.env` file and fill with real values:

```bash
# Get token from https://plugins.jetbrains.com/author/me
PUBLISH_TOKEN=perm:your_actual_token_here

# Password you used when creating the key
PRIVATE_KEY_PASSWORD=your_actual_password

# Contents of private.pem file (copy as is, including headers)
PRIVATE_KEY=-----BEGIN RSA PRIVATE KEY-----
MIIJKgIBAAKCAgEAwU8awS22Rw902BmwVDDBMlTREX440BAAVM40NW3E0lJ7YTJG
... (your actual key) ...
EnNBfIVFhh6khisKqTBWSEo5iS2RYJcuZs961riCn1LARztiaXL4l17oW8t+Qw==
-----END RSA PRIVATE KEY-----

# Contents of chain.crt file (copy as is, including headers)
CERTIFICATE_CHAIN=-----BEGIN CERTIFICATE-----
MIIElgCCAn4CCQDo83LWYj2QSTANBgkqhkiG9w0BAQsFADANMQswCQYDVQQGEwJQ
... (your actual certificate) ...
gdZzxCN8t1EmH8kD2Yve6YKGFCRAIIzveEg=
-----END CERTIFICATE-----
```

### Step 3: Get Publishing Token

1. Go to https://plugins.jetbrains.com/author/me
2. In "API Tokens" section create a new token
3. Copy the token and add it to `.env` file

### Step 4: Test Signing

**Important**: Make sure your `.env` file contains proper multiline format:

```env
PRIVATE_KEY=-----BEGIN RSA PRIVATE KEY-----
MIIJKgIBAAKCAgEAwU8awS22Rw902BmwVDDBMlTREX440BAAVM40NW3E0lJ7YTJG
...your actual key...
EnNBfIVFhh6khisKqTBWSEo5iS2RYJcuZs961riCn1LARztiaXL4l17oW8t+Qw==
-----END RSA PRIVATE KEY-----

CERTIFICATE_CHAIN=-----BEGIN CERTIFICATE-----
MIIElgCCAn4CCQDo83LWYj2QSTANBgkqhkiG9w0BAQsFADANMQswCQYDVQQGEwJQ
...your actual certificate...
gdZzxCN8t1EmH8kD2Yve6YKGFCRAIIzveEg=
-----END CERTIFICATE-----
```

```bash
# Load environment variables (Linux/Mac)
source .env

# For Windows PowerShell use special script:
.\load-env.ps1

# Build and sign plugin
./gradlew buildPlugin signPlugin

# Verify signature
./gradlew verifyPluginSignature
```

### Step 5: Publishing

```bash
# Publish signed plugin
./gradlew publishPlugin
```

## Alternative Method: Using Files

If you prefer storing keys in files (but NOT in git!), change `build.gradle.kts`:

```kotlin
signPlugin {
    certificateChainFile.set(file("certificate/chain.crt"))
    privateKeyFile.set(file("certificate/private.pem"))
    password.set(providers.environmentVariable("PRIVATE_KEY_PASSWORD"))
}
```

Create `certificate/` folder and place files there.

## Security

⚠️ **IMPORTANT**: Never commit to git:
- Private keys (*.pem)
- Certificates (*.crt) 
- .env file with tokens
- Any files with passwords

✅ **Safe to commit**:
- `env.example` (template without real data)
- `build.gradle.kts` configuration
- Updated `.gitignore`

## Status Check

After successful signing and publishing:
1. Plugin will be available on marketplace
2. No security warnings during installation
3. JetBrains will additionally sign plugin with their key

## Troubleshooting

**Error "Certificate chain is empty"**:
- Check correctness of CERTIFICATE_CHAIN content
- Make sure you included BEGIN/END CERTIFICATE headers

**Error "Invalid private key"**:
- Check correctness of PRIVATE_KEY content
- Make sure you included BEGIN/END RSA PRIVATE KEY headers
- Verify password correctness

**Publishing error**:
- Make sure PUBLISH_TOKEN is valid
- Check marketplace access rights 