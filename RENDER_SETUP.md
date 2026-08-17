Render deployment steps (recommended)

1) Create Render account
- Visit https://render.com and sign up.

2) Create a new Web Service
- Click New -> Web Service
- Connect your GitHub account and select the repository (the repo that contains this project)
- For Environment choose: Docker
- Branch: main (or the branch you prefer)
- Region: choose nearest
- Plan: Starter (or as needed)
- Build Command: leave empty (Dockerfile is used)
- Start Command: leave empty (Dockerfile defines image command)

3) Environment variables (IMPORTANT)
- Add YOUTUBE_API_KEY and set its Value to your secret key
- Make sure it's added for the environment(s) you want (Production/Preview)

4) Deploy
- Click Create Web Service — Render will read Dockerfile and build the image
- Watch the build logs; if it fails, open the build log to see errors

5) Notes & tips
- Local test: create a local .env file with YOUTUBE_API_KEY and run: mvn spring-boot:run
- If Docker build is slow on Render, consider using a smaller base image or prebuilding/pushing image to a registry
- If you committed the API key, rotate it in Google Cloud and remove from Git history

6) Optional: use render.yaml
- Place the provided render.yaml at the repo root and then you can import this YAML in Render (New -> Import from Render). Update repo URL and name before importing.
