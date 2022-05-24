select distinct
     playlist.Name,
    track.Name
from track 
inner join playlisttrack on playlisttrack.TrackId = track.TrackId
inner join playlist on playlist.PlaylistId = playlisttrack.PlaylistId
where track.Milliseconds >= 180 and track.UnitPrice = 0.99;

select distinct
	album.Title
from customer 
inner join invoice on customer.CustomerId = invoice.CustomerId
inner join invoiceline on invoice.InvoiceId= invoiceline.InvoiceId
inner join track on invoiceline.TrackId = track.TrackId
left join album on track.AlbumId = album.AlbumId
where invoice.InvoiceDate between '2013-01-01 00:00:00' and '2013-12-31 00:00:00';