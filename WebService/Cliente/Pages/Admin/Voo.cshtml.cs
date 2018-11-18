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
    public class AdminVooModel : PageModel
    {
        public string Message { get; set; }
        public VooCollection voos { get; set; }
    
        /**
         * Ao receber um GET, requisita todos os voos registrados do WebServer.
         */
        public async Task OnGet()
        {
            Message = "Página de administração de voos";

            HttpClient httpClient = getNewClient();
            await getVooList(httpClient);
            httpClient.Dispose();
        }

        /**
         * Ao receber um POST, envia o novo voo para o servidor e requisita a lista de voos registrados.
         */
        public async Task OnPost()
        {
            Message = "Página de administração de voos";

            HttpClient httpClient = getNewClient();
            Voo voo = new Voo(DateTime.Parse(Request.Form["data"]),Request.Form["origem"],Request.Form["destino"],Request.Form["companhia"],int.Parse(Request.Form["preco"]),int.Parse(Request.Form["assentosVagos"]));

            HttpResponseMessage response = await httpClient.PostAsXmlAsync(Constants.serverPath+"/admin/voo",voo);
            await getVooList(httpClient);
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
         * Requisita a lista de voos registrados
         */
        private async Task getVooList(HttpClient client){
            HttpResponseMessage response = await client.GetAsync(Constants.serverPath+"/admin/voo");

            if(response.IsSuccessStatusCode)
                voos=await response.Content.ReadAsAsync<VooCollection>(new List<MediaTypeFormatter>{
                                                                new XmlMediaTypeFormatter(), 
                                                                new JsonMediaTypeFormatter()});
        }
    }
}
