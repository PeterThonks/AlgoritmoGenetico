select distinct
     playlist.Name,
    track.Name
from track 
inner join playlisttrack on playlisttrack.TrackId = track.TrackId
inner join playlist on playlist.PlaylistId = playlisttrack.PlaylistId
where track.Milliseconds >= 180 and track.UnitPrice = 0.99;

select distinct
     genre.Name
from customer 
inner join invoice on customer.CustomerId = invoice.CustomerId
inner join invoiceline on invoice.InvoiceId= invoiceline.InvoiceId
inner join track on invoiceline.TrackId = track.TrackId
inner join genre on track.GenreId = genre.GenreId
where customer.LastName = 'Harris';