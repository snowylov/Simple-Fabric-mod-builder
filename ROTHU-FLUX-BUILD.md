# Rothu Flux v1.49.0 GitHub Build Branch

This branch is a dedicated GitHub Actions build harness for **Rothu Flux v1.49.0**.

## One file still needs to be uploaded

Upload this conversation artifact to this branch after base64-encoding it:

- Source archive: `rothuflux-v1.49.0-github-build-source.zip`
- Required branch filename: `rothuflux-v1.49.0-source.zip.b64`
- SHA-256 of the decoded ZIP:
  `f7111e982d71bfb1bcb9edbc1c2573b9bf102d955a877afbaaddbf2330fcbfd4`

### Convert the ZIP to the required text bundle

Linux/macOS:

```bash
base64 rothuflux-v1.49.0-github-build-source.zip | tr -d '\n' > rothuflux-v1.49.0-source.zip.b64
```

Windows PowerShell:

```powershell
[Convert]::ToBase64String([IO.File]::ReadAllBytes("rothuflux-v1.49.0-github-build-source.zip")) | Set-Content -NoNewline rothuflux-v1.49.0-source.zip.b64
```

Then commit the `.b64` file to this branch. The workflow will start automatically.

## Build outputs

The workflow is configured to produce and verify:

- `rothu-flux-1.49.0-game-ready.jar`
- `rothu-flux-1.49.0-deobf.jar`
- `rothu-flux-1.49.0-api.jar`
- `rothu-flux-1.49.0-src.zip`
- `SHA256SUMS.txt`

## Build environment

- Minecraft 1.21.11
- Java 21
- Fabric Loader 0.18.4
- Fabric API 0.141.6+1.21.11
- Yarn 1.21.11+build.4
- Gradle 8.14.3

The workflow also performs static checks for the Crop Gene Table's 80 RFx edit cost and checks the custom GUI source for accidental duplicate background-blur calls.
