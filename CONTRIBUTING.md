## Development Setup:
- You need JDK 17 or higher.
- You need Gradle 8.2+.
- Go to https://rawg.io/apidocs to Optain your API key.
- Add `RAWG_API_KEY=<YOUR_API_KEY>` to local.properties file, and replace `<YOUR_API_KEY>` with your obtained API key.
- Run `./gradlew generateNonAndroidBuildConfig`.

## Submit A contribution:
1. Open an issue describing the bug or the feature request.
2. Fork the repository.
3. Make a new branch `git checkout -b new_feature`.
4. Make your changes.
5. Commit changes, `git add .` then `git commit -m "Commit Message"`.
6. push the changes upstream to your fork `git push origin new_feature`.
7. Open a PR to merge the changes with the original Repository.