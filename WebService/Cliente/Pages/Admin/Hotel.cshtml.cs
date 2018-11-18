using System;
using System.Threading.Tasks;
using RazorPagesMovie.Models;
using System.Net.Http.Formatting;
using System.Net.Http.Headers;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using System.Net.Http;
using System.Collections.Generic;

namespace RazorPagesMovie.Pages
{
    public class AdminHotelModel : PageModel
    {
        public string Message { get; set; }
        public HotelCollection hotels { get; set; }

        /**
         * Ao receber um GET, requisita todos os hotéis registrados do WebServer.
         */
        public async Task OnGet()
        {
            Message = "Página de administração de hotéis";

            HttpClient httpClient = getNewClient();
            await getHotelList(httpClient);
            httpClient.Dispose();
        }

        /**
         * Ao receber um POST, envia o novo hotel para o servidor e requisita a lista de hotéis registrados.
         */
        public async Task OnPost()
        {
            Message = "Página de administração de hotéis";

            HttpClient httpClient = getNewClient();
            Hotel hotel = new Hotel(Request.Form["nome"],Request.Form["destino"],int.Parse(Request.Form["precoQuarto"]),int.Parse(Request.Form["quartosVagos"]));

            HttpResponseMessage response = await httpClient.PostAsXmlAsync(Constants.serverPath+"/admin/hotel",hotel);
            await getHotelList(httpClient);
            httpClient.Dispose();
        }

        /**
         * Inicia o cliente HTTP
         */
        private HttpClient getNewClient()
        {
            HttpClient newClient = new HttpClient();
            newClient.DefaultRequestHeaders.Accept.Clear();
            newClient.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/xml"));
            newClient.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("text/plain"));

            return newClient;
        }

        /**
         * Requisita a lista de hotéis registrados
         */
        private async Task getHotelList(HttpClient client){
            HttpResponseMessage response = await client.GetAsync(Constants.serverPath+"/admin/hotel");

            if(response.IsSuccessStatusCode)
                hotels=await response.Content.ReadAsAsync<HotelCollection>(new List<MediaTypeFormatter>{
                                                                new XmlMediaTypeFormatter(), 
                                                                new JsonMediaTypeFormatter()});
        }
    }
}
