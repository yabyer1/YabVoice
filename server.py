from flask import Flask, request, jsonify
import faster_whisper

app = Flask(__name__)
model = faster_whisper.WhisperModel("small.en")

@app.route("/transcribe", methods=["POST"])
def transcribe():
    if "file" not in request.files:
        return jsonify({"error": "No file uploaded"}), 400

    audio_file = request.files["file"]
    segments, _ = model.transcribe(audio_file)

    transcription = " ".join([segment.text for segment in segments])
    return jsonify({"transcription": transcription})

if __name__ == "__main__":
    app.run(port=8080)

#command :  curl http://127.0.0.1:8080/transcribe \ -H "Content-Type: multipart/form-data" \ -F file=@./samples/jfk.wav -F response-format="json" | jq
#change file to whatever when the time comes
