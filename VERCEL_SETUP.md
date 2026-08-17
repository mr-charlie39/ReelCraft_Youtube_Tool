Vercel deployment notes

1) Env var name (IMPORTANT)
- In Vercel, add an Environment Variable with Name: YOUTUBE_API_KEY
- Value: <your-secret-key>  (do NOT include ${} in the Name)
- Select: Production and Preview

2) Quick deploy (Vercel CLI)
- vercel env add YOUTUBE_API_KEY production
- vercel --prod

3) Local testing
- PowerShell: $env:YOUTUBE_API_KEY="your_key"; mvn spring-boot:run
- cmd.exe: set YOUTUBE_API_KEY=your_key && mvn spring-boot:run

4) Notes
- application.yaml should use: key: ${YOUTUBE_API_KEY}
- If a key was committed, rotate it and purge history.
