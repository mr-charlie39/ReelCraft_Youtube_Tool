# 🎯 HOW TO USE THE APPLICATION

## YouTube SEO Tag Generator

### Step-by-Step Guide:

1. **Open the Application**
   - Visit: http://localhost:8080/
   - You'll see the main page with "YouTube Video Title" input field

2. **Enter a Video Title**
   - Click in the input field
   - Type a YouTube video title (e.g., "React Tutorial", "JavaScript Basics", "Web Development")
   - ✅ The "Generate SEO Tags" button will automatically **enable** as you type

3. **Click Generate SEO Tags**
   - Once you've entered a title, the button becomes clickable
   - Click the button to search
   - ⏳ You'll see "Searching..." with a loading spinner

4. **View Results**
   - The application will:
     - Search YouTube for videos matching your title
     - Extract the primary video's tags
     - Extract tags from related/similar videos
   - Results display below the form

5. **Copy Tags to Clipboard**
   - Click **"Copy Tags"** button on Primary Video
   - Click **"Copy All"** to copy tags from all related videos
   - ✅ You'll see "Tags copied to clipboard!" message

---

## Thumbnail Generator

### Step-by-Step Guide:

1. **Navigate to Thumbnail Generator**
   - Click the "Thumbnail Generator" link or visit: http://localhost:8080/thumbnail

2. **Enter YouTube URL or Video ID**
   - You can enter EITHER:
     - **Full URL**: `https://www.youtube.com/watch?v=dQw4w9WgXcQ`
     - **Short URL**: `https://youtu.be/dQw4w9WgXcQ`
     - **Video ID only**: `dQw4w9WgXcQ`

3. **Click Get Thumbnail**
   - The button enables once you type
   - Click to extract the highest quality thumbnail

4. **Download Thumbnail**
   - Right-click on the thumbnail image
   - Select "Save image as..."
   - Choose your location and save

---

## Video Details

### Navigate to Video Details
- Click "Video Details" link or visit: http://localhost:8080/video-details
- View comprehensive video metadata and information

---

## IMPORTANT TROUBLESHOOTING

### "Button is Disabled / Greyed Out"
✅ **Solution**: TYPE something in the input field first!
- The button enables only when you type valid input
- This is a security feature to prevent empty submissions

### "No Results Appearing"
Check these:
1. ✅ Make sure you **typed a video title** (not just clicked the button)
2. ✅ Press ENTER or click the button **after** typing
3. ✅ Wait 2-3 seconds for the API response
4. ✅ Check internet connection
5. ✅ Verify the YouTube API key in `application.yaml` is valid

### API Key Issues
If you get an error about API key:
1. Check: `src/main/resources/application.yaml`
2. Verify the YouTube API key is present
3. Ensure the key has proper permissions

---

## 📝 Example Searches

Try these video titles:

1. **"React Tutorial"**
   - Returns multiple React tutorial videos
   - Shows tags like: react, javascript, tutorial, web development

2. **"JavaScript Basics"**
   - Shows JavaScript beginner content
   - Tags include: javascript, programming, tutorial

3. **"Python For Beginners"**
   - Shows Python learning content
   - Tags include: python, programming, tutorial

4. **"Web Design Tips"**
   - Shows web design videos
   - Tags include: web design, tutorial, css, html

---

## 🔧 How It Works

### Backend Flow:
1. **User enters video title** → Click button
2. **Server receives request** → `/youtube/search?videoTitle=...`
3. **YouTube API is called** → Search for videos
4. **Parse results** → Extract video IDs
5. **Fetch metadata** → Get video details and tags
6. **Return to UI** → Display results

### Frontend Flow:
1. **User types** → Button enables/disables automatically
2. **User submits** → Loading spinner appears
3. **Form posts data** → Server processes
4. **Results render** → Tags display below
5. **User can copy** → One-click clipboard copy

---

## 💡 Tips

✅ **Use specific titles** for better results
- Good: "React Hooks Tutorial 2024"
- Bad: "video"

✅ **Wait for results** - API calls take 2-3 seconds

✅ **Copy multiple sets** - Get tags from different videos

✅ **Combine tags** - Mix primary and related video tags for better SEO

---

**Need Help?**
- Check the browser console (F12) for error messages
- Check application logs for API errors
- Verify YouTube API key is valid
- Ensure internet connection is active

