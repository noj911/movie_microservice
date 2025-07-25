# Movie Streaming Application with Amazon S3 Integration

This application is a movie streaming platform that allows users to manage and stream movies and TV series. It has been enhanced with Amazon S3 integration for storing and streaming video content.

## Features

- Manage movies and TV series with their metadata
- Upload video content to Amazon S3
- Associate video content with episodes
- Stream video content from Amazon S3
- Support for multiple video qualities (SD, HD, FHD, 4K)

## Architecture

The application is built with:

- **Spring Boot 3.4.5**: Backend framework
- **Java 21**: Programming language
- **MongoDB**: NoSQL database for storing metadata
- **Amazon S3**: Cloud storage for video content
- **GraphQL**: API for managing metadata
- **REST**: API for file operations

## Configuration

### AWS S3 Configuration

To use the Amazon S3 integration, you need to configure the following properties in `application.properties`:

```properties
aws.accessKey=your_access_key_here
aws.secretKey=your_secret_key_here
aws.region=your_region_here
aws.s3.bucket=your_bucket_name_here
```

Replace the placeholder values with your actual AWS credentials and bucket information.

### File Upload Configuration

The application is configured to accept files up to 100MB in size:

```properties
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB
```

You can adjust these values based on your requirements.

## API Endpoints

### GraphQL Endpoints

The application exposes a GraphQL API at `/graphql` for managing series, seasons, and episodes:

- **Queries**:
  - `getSerieById`: Get a series by ID
  - `getAllSeries`: Get all series
  - `getSeriesByCriteria`: Filter series by criteria
  - `getSeriesByCategory`: Get series by category

- **Mutations**:
  - `createSerie`: Create a new series
  - `updateSerie`: Update an existing series
  - `deleteSerie`: Delete a series
  - `addSeason`: Add a season to a series
  - `addEpisode`: Add an episode to a season

### REST Endpoints for File Operations

The application exposes REST endpoints for file operations:

- **POST /api/storage/upload**: Upload a file to S3
  - Parameters:
    - `file`: The file to upload
    - `qualite`: The quality of the video (SD_480P, HD_720P, FHD_1080P, UHD_2160P)
  - Returns: VideoMetadata

- **POST /api/storage/upload/episode**: Upload a file and associate it with an episode
  - Parameters:
    - `file`: The file to upload
    - `serieId`: The ID of the series
    - `saisonNumero`: The number of the season
    - `episodeNumero`: The number of the episode
    - `qualite`: The quality of the video (SD_480P, HD_720P, FHD_1080P, UHD_2160P)
  - Returns: VideoMetadata

- **POST /api/storage/upload/episode/quality**: Add a new quality version to an existing episode's video
  - Parameters:
    - `file`: The file to upload
    - `serieId`: The ID of the series
    - `saisonNumero`: The number of the season
    - `episodeNumero`: The number of the episode
    - `qualite`: The quality of the video (SD_480P, HD_720P, FHD_1080P, UHD_2160P)
  - Returns: Updated VideoMetadata with the new quality

- **DELETE /api/storage/delete**: Delete a file from S3
  - Parameters:
    - `path`: The path of the file in S3
  - Returns: 200 OK if successful

- **GET /api/storage/url**: Get the URL for accessing a file
  - Parameters:
    - `path`: The path of the file in S3
  - Returns: The URL for accessing the file

## Usage Examples

### Uploading a Video for an Episode

```bash
curl -X POST \
  http://localhost:8085/api/storage/upload/episode \
  -H 'Content-Type: multipart/form-data' \
  -F 'file=@/path/to/video.mp4' \
  -F 'serieId=123' \
  -F 'saisonNumero=1' \
  -F 'episodeNumero=1' \
  -F 'qualite=HD_720P'
```

### Adding a New Quality Version to an Existing Episode

```bash
curl -X POST \
  http://localhost:8085/api/storage/upload/episode/quality \
  -H 'Content-Type: multipart/form-data' \
  -F 'file=@/path/to/video_4k.mp4' \
  -F 'serieId=123' \
  -F 'saisonNumero=1' \
  -F 'episodeNumero=1' \
  -F 'qualite=UHD_2160P'
```

### Getting the URL for a Video

```bash
curl -X GET \
  'http://localhost:8085/api/storage/url?path=videos/HD_720P/filename.mp4'
```

## Security Considerations

- Store AWS credentials securely, preferably using environment variables or a secrets manager
- Configure proper CORS settings for production
- Implement authentication and authorization for API endpoints
- Set up proper IAM policies for the AWS S3 bucket

## Future Enhancements

- Implement video transcoding for different qualities
- Add support for video streaming protocols like HLS or DASH
- Implement content delivery network (CDN) integration
- Add video analytics and metrics
